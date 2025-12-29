// Landing page functionality
class LandingPage {
    constructor() {
        this.api = apiClient;
        this.currentPage = 0;
        this.pageSize = 12;
        this.currentFilters = {};
        this.init();
    }

    init() {
        document.addEventListener('DOMContentLoaded', () => {
            this.loadFeaturedAnimals();
            this.loadAllAnimals();
            this.loadStatistics();
            this.setupEventListeners();
        });
    }

    setupEventListeners() {
        // Search functionality
        const searchBtn = document.getElementById('searchBtn');
        const heroSearchBtn = document.getElementById('heroSearchBtn');
        const heroSearch = document.getElementById('heroSearch');
        const searchInput = document.getElementById('searchInput');

        const performSearch = (keyword) => {
            if (keyword.trim()) {
                this.searchAnimals(keyword);
            }
        };

        if (searchBtn && searchInput) {
            searchBtn.addEventListener('click', () => performSearch(searchInput.value));
            searchInput.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') performSearch(searchInput.value);
            });
        }

        if (heroSearchBtn && heroSearch) {
            heroSearchBtn.addEventListener('click', () => performSearch(heroSearch.value));
            heroSearch.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') performSearch(heroSearch.value);
            });
        }

        // Filter buttons
        document.querySelectorAll('.filter-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                document.querySelectorAll('.filter-btn').forEach(b => b.classList.remove('active'));
                e.target.classList.add('active');
                const status = e.target.dataset.status;
                if (status === 'all') {
                    this.currentFilters = {};
                } else {
                    this.currentFilters.conservationStatus = status;
                }
                this.loadAllAnimals();
            });
        });

        // Category links
        document.querySelectorAll('[data-category]').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const category = e.target.dataset.category;
                this.searchAnimals(category);
            });
        });

        // Apply filters button
        const applyFiltersBtn = document.getElementById('applyFilters');
        if (applyFiltersBtn) {
            applyFiltersBtn.addEventListener('click', () => {
                this.applyFilters();
            });
        }

        // Sort select
        const sortSelect = document.getElementById('sortSelect');
        if (sortSelect) {
            sortSelect.addEventListener('change', () => {
                this.loadAllAnimals();
            });
        }

        // Learn more button
        const learnMoreBtn = document.getElementById('learnMoreBtn');
        if (learnMoreBtn) {
            learnMoreBtn.addEventListener('click', () => {
                this.showConservationInfo();
            });
        }
    }

    applyFilters() {
        this.currentFilters = {};

        const region = document.getElementById('regionFilter').value;
        const status = document.getElementById('statusFilter').value;
        const island = document.getElementById('islandFilter').value;

        if (region) this.currentFilters.region = region;
        if (status) this.currentFilters.conservationStatus = status;
        if (island) this.currentFilters.island = island;

        this.loadAllAnimals();
    }

    async loadFeaturedAnimals() {
        const container = document.getElementById('featuredAnimals');
        if (!container) return;

        try {
            container.innerHTML = '<div class="col-12 text-center"><div class="spinner-border text-success"></div></div>';

            const data = await this.api.getPublicFeaturedAnimals();

            if (data.featured && data.featured.length > 0) {
                container.innerHTML = data.featured.map(animal => `
                    <div class="col-md-4 col-lg-2 mb-4">
                        <div class="card animal-card h-100">
                            <div class="position-relative">
                                <img src="${animal.imageUrl || 'https://images.unsplash.com/photo-1546182990-dffeafbe841d?ixlib=rb-1.2.1&auto=format&fit=crop&w=400&q=80'}" 
                                     class="card-img-top animal-img" alt="${animal.commonName}">
                                <span class="badge badge-status ${this.getStatusClass(animal.conservationStatus)}">
                                    ${animal.conservationStatus || 'Unknown'}
                                </span>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">${animal.commonName}</h5>
                                <p class="card-text text-muted">${animal.scientificName}</p>
                                <button class="btn btn-sm btn-outline-success view-animal-btn" 
                                        data-id="${animal.id}">
                                    Learn More
                                </button>
                            </div>
                        </div>
                    </div>
                `).join('');

                // Add event listeners to view buttons
                container.querySelectorAll('.view-animal-btn').forEach(btn => {
                    btn.addEventListener('click', (e) => {
                        const animalId = e.target.dataset.id;
                        this.showAnimalDetails(animalId);
                    });
                });
            } else {
                container.innerHTML = '<div class="col-12 text-center"><p>No featured animals found.</p></div>';
            }
        } catch (error) {
            console.error('Error loading featured animals:', error);
            container.innerHTML = '<div class="col-12 text-center"><p>Error loading featured animals.</p></div>';
        }
    }

    async loadAllAnimals(page = this.currentPage) {
        const container = document.getElementById('animalsGrid');
        const pagination = document.getElementById('pagination');

        if (!container) return;

        try {
            container.innerHTML = '<div class="col-12 text-center"><div class="spinner-border text-success"></div></div>';

            let data;

            if (Object.keys(this.currentFilters).length > 0) {
                // Use advanced search with filters
                data = await this.api.advancedSearch({
                    ...this.currentFilters,
                    page: page,
                    size: this.pageSize
                });

                // Format data to match expected structure
                let animals = [];
                if (data.byKeyword) animals = animals.concat(data.byKeyword);
                if (data.byRegion) animals = animals.concat(data.byRegion);
                if (data.byConservationStatus) animals = animals.concat(data.byConservationStatus);
                if (data.byIsland) animals = animals.concat(data.byIsland);

                data = {
                    content: [...new Map(animals.map(item => [item.id, item])).values()],
                    totalElements: animals.length,
                    totalPages: Math.ceil(animals.length / this.pageSize),
                    currentPage: page
                };
            } else {
                // Get all animals
                data = await this.api.getAnimals(page, this.pageSize);
            }

            // Sort animals
            const sortBy = document.getElementById('sortSelect')?.value || 'name';
            data.content.sort((a, b) => {
                switch(sortBy) {
                    case 'name':
                        return a.commonName.localeCompare(b.commonName);
                    case 'date':
                        return new Date(b.createdAt) - new Date(a.createdAt);
                    case 'status':
                        return this.getStatusPriority(a.conservationStatus) - this.getStatusPriority(b.conservationStatus);
                    default:
                        return 0;
                }
            });

            if (data.content && data.content.length > 0) {
                container.innerHTML = data.content.map(animal => `
                    <div class="col-md-6 col-lg-4 col-xl-3 mb-4">
                        <div class="card animal-card h-100">
                            <div class="position-relative">
                                <img src="${animal.imageUrl || 'https://images.unsplash.com/photo-1546182990-dffeafbe841d?ixlib=rb-1.2.1&auto=format&fit=crop&w=400&q=80'}" 
                                     class="card-img-top animal-img" alt="${animal.commonName}">
                                <span class="badge badge-status ${this.getStatusClass(animal.conservationStatus)}">
                                    ${animal.conservationStatus || 'Unknown'}
                                </span>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">${animal.commonName}</h5>
                                <h6 class="card-subtitle mb-2 text-muted">${animal.scientificName}</h6>
                                <p class="card-text">${this.truncateText(animal.description || 'No description available', 100)}</p>
                                <div class="d-flex justify-content-between">
                                    <small class="text-muted">
                                        <i class="bi bi-geo-alt"></i> ${animal.region || 'Unknown region'}
                                    </small>
                                    <small class="text-muted">
                                        <i class="bi bi-island"></i> ${animal.island || 'Unknown island'}
                                    </small>
                                </div>
                            </div>
                            <div class="card-footer bg-transparent border-top-0">
                                <button class="btn btn-sm btn-outline-success w-100 view-animal-btn" 
                                        data-id="${animal.id}">
                                    View Details
                                </button>
                            </div>
                        </div>
                    </div>
                `).join('');

                // Add event listeners to view buttons
                container.querySelectorAll('.view-animal-btn').forEach(btn => {
                    btn.addEventListener('click', (e) => {
                        const animalId = e.target.dataset.id;
                        this.showAnimalDetails(animalId);
                    });
                });

                // Update pagination
                this.updatePagination(data.totalPages, page, pagination);
            } else {
                container.innerHTML = '<div class="col-12 text-center"><p>No animals found. Try different filters.</p></div>';
                if (pagination) pagination.innerHTML = '';
            }
        } catch (error) {
            console.error('Error loading animals:', error);
            container.innerHTML = '<div class="col-12 text-center"><p>Error loading animals. Please try again later.</p></div>';
        }
    }

    async searchAnimals(keyword) {
        const container = document.getElementById('animalsGrid');
        if (!container) return;

        try {
            container.innerHTML = '<div class="col-12 text-center"><div class="spinner-border text-success"></div></div>';

            const data = await this.api.searchPublicAnimals(keyword);

            if (data.results && data.results.length > 0) {
                container.innerHTML = data.results.map(animal => `
                    <div class="col-md-6 col-lg-4 col-xl-3 mb-4">
                        <div class="card animal-card h-100">
                            <div class="position-relative">
                                <img src="${animal.imageUrl || 'https://images.unsplash.com/photo-1546182990-dffeafbe841d?ixlib=rb-1.2.1&auto=format&fit=crop&w=400&q=80'}" 
                                     class="card-img-top animal-img" alt="${animal.commonName}">
                                <span class="badge badge-status ${this.getStatusClass(animal.conservationStatus)}">
                                    ${animal.conservationStatus || 'Unknown'}
                                </span>
                            </div>
                            <div class="card-body">
                                <h5 class="card-title">${animal.commonName}</h5>
                                <h6 class="card-subtitle mb-2 text-muted">${animal.scientificName}</h6>
                                <p class="card-text">${this.truncateText(animal.description || 'No description available', 100)}</p>
                                <button class="btn btn-sm btn-outline-success view-animal-btn" 
                                        data-id="${animal.id}">
                                    View Details
                                </button>
                            </div>
                        </div>
                    </div>
                `).join('');

                // Add event listeners
                container.querySelectorAll('.view-animal-btn').forEach(btn => {
                    btn.addEventListener('click', (e) => {
                        const animalId = e.target.dataset.id;
                        this.showAnimalDetails(animalId);
                    });
                });

                // Update search input
                const searchInput = document.getElementById('searchInput');
                const heroSearch = document.getElementById('heroSearch');
                if (searchInput) searchInput.value = keyword;
                if (heroSearch) heroSearch.value = keyword;

                // Scroll to results
                document.getElementById('animals').scrollIntoView({ behavior: 'smooth' });
            } else {
                container.innerHTML = '<div class="col-12 text-center"><p>No animals found for "' + keyword + '".</p></div>';
            }
        } catch (error) {
            console.error('Error searching animals:', error);
            container.innerHTML = '<div class="col-12 text-center"><p>Error searching animals.</p></div>';
        }
    }

    async showAnimalDetails(animalId) {
        try {
            const animal = await this.api.getAnimalById(animalId);

            const modalTitle = document.getElementById('animalModalTitle');
            const modalBody = document.getElementById('animalModalBody');

            if (modalTitle && modalBody) {
                modalTitle.textContent = `${animal.commonName} (${animal.scientificName})`;

                modalBody.innerHTML = `
                    <div class="row">
                        <div class="col-md-6">
                            <img src="${animal.imageUrl || 'https://images.unsplash.com/photo-1546182990-dffeafbe841d?ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=80'}" 
                                 class="img-fluid rounded mb-3" alt="${animal.commonName}">
                            <div class="d-flex flex-wrap gap-2 mb-3">
                                <span class="badge bg-success">${animal.region || 'Unknown Region'}</span>
                                <span class="badge bg-info">${animal.island || 'Unknown Island'}</span>
                                <span class="badge ${this.getStatusClass(animal.conservationStatus)}">
                                    ${animal.conservationStatus || 'Unknown Status'}
                                </span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <h5>Description</h5>
                            <p>${animal.description || 'No description available.'}</p>
                            
                            <h5 class="mt-4">Characteristics</h5>
                            <p>${animal.characteristics || 'No characteristics information.'}</p>
                            
                            <h5 class="mt-4">Habitat</h5>
                            <p>${animal.habitat || 'No habitat information.'}</p>
                            
                            <h5 class="mt-4">Diet</h5>
                            <p>${animal.diet || 'No diet information.'}</p>
                            
                            <h5 class="mt-4">Conservation Status</h5>
                            <div class="alert ${this.getStatusAlertClass(animal.conservationStatus)}">
                                <strong>${animal.conservationStatus || 'Unknown'}</strong>
                                <p class="mb-0">${this.getConservationMessage(animal.conservationStatus)}</p>
                            </div>
                        </div>
                    </div>
                `;

                const modal = new bootstrap.Modal(document.getElementById('animalModal'));
                modal.show();
            }
        } catch (error) {
            console.error('Error loading animal details:', error);
            authManager.showNotification('Error loading animal details', 'danger');
        }
    }

    async loadStatistics() {
        try {
            const data = await this.api.getAdminStats();

            // Update statistics in conservation section
            const endangeredCount = document.getElementById('endangeredCount');
            const protectedCount = document.getElementById('protectedCount');
            const totalAnimals = document.getElementById('totalAnimals');

            if (endangeredCount) endangeredCount.textContent = data.endangeredCount || '0';
            if (protectedCount) protectedCount.textContent = data.protectedCount || '0';
            if (totalAnimals) totalAnimals.textContent = data.totalAnimals || '0';
        } catch (error) {
            console.error('Error loading statistics:', error);
        }
    }

    updatePagination(totalPages, currentPage, container) {
        if (!container || totalPages <= 1) {
            container.innerHTML = '';
            return;
        }

        let html = '';

        // Previous button
        html += `
            <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage - 1}">Previous</a>
            </li>
        `;

        // Page numbers
        const maxVisible = 5;
        let start = Math.max(0, currentPage - Math.floor(maxVisible / 2));
        let end = Math.min(totalPages - 1, start + maxVisible - 1);

        if (end - start + 1 < maxVisible) {
            start = Math.max(0, end - maxVisible + 1);
        }

        for (let i = start; i <= end; i++) {
            html += `
                <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                </li>
            `;
        }

        // Next button
        html += `
            <li class="page-item ${currentPage >= totalPages - 1 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage + 1}">Next</a>
            </li>
        `;

        container.innerHTML = html;

        // Add event listeners
        container.querySelectorAll('.page-link').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const page = parseInt(e.target.dataset.page);
                if (!isNaN(page) && page >= 0 && page < totalPages) {
                    this.currentPage = page;
                    this.loadAllAnimals(page);
                    window.scrollTo({ top: document.getElementById('animals').offsetTop - 100, behavior: 'smooth' });
                }
            });
        });
    }

    showConservationInfo() {
        const modalBody = `
            <div class="conservation-info">
                <h4>Conservation Efforts in the Philippines</h4>
                <p>The Philippines is one of the world's 17 megadiverse countries, but also one of the world's biodiversity hotspots facing enormous threats.</p>
                
                <h5>Key Threats:</h5>
                <ul>
                    <li><strong>Habitat Loss:</strong> Deforestation for agriculture and development</li>
                    <li><strong>Climate Change:</strong> Rising temperatures and extreme weather events</li>
                    <li><strong>Illegal Wildlife Trade:</strong> Poaching and trafficking of endangered species</li>
                    <li><strong>Pollution:</strong> Plastic pollution and agricultural runoff</li>
                </ul>
                
                <h5>How Animalphidia Helps:</h5>
                <ul>
                    <li>Documenting and monitoring wildlife populations</li>
                    <li>Educating the public about conservation</li>
                    <li>Providing data for research and policy-making</li>
                    <li>Connecting conservationists and volunteers</li>
                </ul>
                
                <div class="alert alert-success mt-3">
                    <strong>Get Involved:</strong> Join our community of contributors to help document and protect Philippine wildlife.
                </div>
            </div>
        `;

        const modal = new bootstrap.Modal(document.createElement('div'));
        modal._element.className = 'modal fade';
        modal._element.innerHTML = `
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Conservation Information</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        ${modalBody}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" data-bs-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        `;

        document.body.appendChild(modal._element);
        modal.show();

        modal._element.addEventListener('hidden.bs.modal', () => {
            document.body.removeChild(modal._element);
        });
    }

    // Helper methods
    getStatusClass(status) {
        if (!status) return 'bg-secondary';
        const statusLower = status.toLowerCase();
        if (statusLower.includes('critically') || statusLower.includes('endangered')) return 'bg-danger';
        if (statusLower.includes('vulnerable')) return 'bg-warning';
        if (statusLower.includes('near threatened')) return 'bg-info';
        if (statusLower.includes('least concern')) return 'bg-success';
        return 'bg-secondary';
    }

    getStatusAlertClass(status) {
        if (!status) return 'alert-secondary';
        const statusLower = status.toLowerCase();
        if (statusLower.includes('critically') || statusLower.includes('endangered')) return 'alert-danger';
        if (statusLower.includes('vulnerable')) return 'alert-warning';
        if (statusLower.includes('near threatened')) return 'alert-info';
        if (statusLower.includes('least concern')) return 'alert-success';
        return 'alert-secondary';
    }

    getStatusPriority(status) {
        if (!status) return 99;
        const statusLower = status.toLowerCase();
        if (statusLower.includes('critically')) return 1;
        if (statusLower.includes('endangered')) return 2;
        if (statusLower.includes('vulnerable')) return 3;
        if (statusLower.includes('near threatened')) return 4;
        if (statusLower.includes('least concern')) return 5;
        return 99;
    }

    getConservationMessage(status) {
        if (!status) return 'Conservation status unknown.';
        const statusLower = status.toLowerCase();
        if (statusLower.includes('critically')) return 'This species faces an extremely high risk of extinction in the wild.';
        if (statusLower.includes('endangered')) return 'This species faces a very high risk of extinction in the wild.';
        if (statusLower.includes('vulnerable')) return 'This species faces a high risk of extinction in the wild.';
        if (statusLower.includes('near threatened')) return 'This species is close to qualifying for threatened status.';
        if (statusLower.includes('least concern')) return 'This species is widespread and abundant.';
        return 'Conservation status information not available.';
    }

    truncateText(text, maxLength) {
        if (text.length <= maxLength) return text;
        return text.substr(0, maxLength) + '...';
    }
}

// Initialize landing page
const landingPage = new LandingPage();