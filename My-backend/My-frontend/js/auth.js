// auth.js - DEBUG VERSION
console.log('🔧 auth.js LOADED - Starting AuthManager...');

// Check if apiClient exists
if (typeof apiClient === 'undefined') {
    console.error('❌ CRITICAL: apiClient is not defined!');
    console.error('❌ Make sure api-client.js is loaded BEFORE auth.js');
    console.error('❌ Current scripts:', Array.from(document.scripts).map(s => s.src).join(', '));

    // Create a fallback apiClient
    window.apiClient = {
        setToken: function(token, refreshToken) {
            console.log('🔧 Fallback: Token set', token ? 'YES' : 'NO');
            localStorage.setItem('token', token || '');
        },
        get: function(url) {
            console.log('🔧 Fallback: GET', url);
            return Promise.resolve([]);
        }
    };
} else {
    console.log('✅ apiClient is defined');
}

class AuthManager {
    constructor() {
        console.log('🔧 AuthManager constructor called');
        console.log('🔧 Current user in localStorage:', localStorage.getItem('user'));
        console.log('🔧 Current token in localStorage:', localStorage.getItem('token') ? 'EXISTS' : 'MISSING');

        this.api = apiClient;
        this.currentUser = JSON.parse(localStorage.getItem('user') || 'null');

        console.log('🔧 AuthManager initialized with user:', this.currentUser);

        this.initEventListeners();
        this.checkAuthStatus();
    }

    initEventListeners() {
        console.log('🔧 Initializing event listeners...');

        // Login button
        const loginBtn = document.getElementById('loginBtn');
        console.log('🔧 Login button found:', !!loginBtn);
        if (loginBtn) {
            loginBtn.addEventListener('click', () => {
                console.log('🔧 Login button clicked');
                this.showLoginModal();
            });
        }

        // Login form submission
        const loginForm = document.getElementById('loginForm');
        console.log('🔧 Login form found:', !!loginForm);
        if (loginForm) {
            loginForm.addEventListener('submit', (e) => {
                e.preventDefault();
                console.log('🔧 Login form submitted');
                this.handleLogin();
            });
        }

        // Register form submission
        const registerForm = document.getElementById('registerForm');
        console.log('🔧 Register form found:', !!registerForm);
        if (registerForm) {
            registerForm.addEventListener('submit', (e) => {
                e.preventDefault();
                console.log('🔧 Register form submitted');
                this.handleRegister();
            });
        }

        // Show register link
        const showRegister = document.getElementById('showRegister');
        console.log('🔧 Show register link found:', !!showRegister);
        if (showRegister) {
            showRegister.addEventListener('click', (e) => {
                e.preventDefault();
                console.log('🔧 Show register clicked');
                this.switchToRegister();
            });
        }

        // Show login link
        const showLogin = document.getElementById('showLogin');
        console.log('🔧 Show login link found:', !!showLogin);
        if (showLogin) {
            showLogin.addEventListener('click', (e) => {
                e.preventDefault();
                console.log('🔧 Show login clicked');
                this.switchToLogin();
            });
        }

        console.log('✅ Event listeners initialized');
    }

    showLoginModal() {
        console.log('🔧 Showing login modal');
        try {
            const loginModalElement = document.getElementById('loginModal');
            console.log('🔧 Login modal element:', !!loginModalElement);
            if (loginModalElement) {
                const loginModal = new bootstrap.Modal(loginModalElement);
                loginModal.show();
                console.log('✅ Login modal shown');
            } else {
                console.error('❌ Login modal element not found');
            }
        } catch (error) {
            console.error('❌ Error showing login modal:', error);
        }
    }

    showRegisterModal() {
        console.log('🔧 Showing register modal');
        try {
            const registerModalElement = document.getElementById('registerModal');
            console.log('🔧 Register modal element:', !!registerModalElement);
            if (registerModalElement) {
                const registerModal = new bootstrap.Modal(registerModalElement);
                registerModal.show();
                console.log('✅ Register modal shown');
            } else {
                console.error('❌ Register modal element not found');
            }
        } catch (error) {
            console.error('❌ Error showing register modal:', error);
        }
    }

    switchToRegister() {
        console.log('🔧 Switching to register modal');

        // Hide login modal
        const loginModalEl = document.getElementById('loginModal');
        if (loginModalEl) {
            const loginModal = bootstrap.Modal.getInstance(loginModalEl);
            if (loginModal) {
                loginModal.hide();
                console.log('✅ Login modal hidden');
            }
        }

        // Show register modal after delay
        setTimeout(() => {
            this.showRegisterModal();
        }, 300);
    }

    switchToLogin() {
        console.log('🔧 Switching to login modal');

        // Hide register modal
        const registerModalEl = document.getElementById('registerModal');
        if (registerModalEl) {
            const registerModal = bootstrap.Modal.getInstance(registerModalEl);
            if (registerModal) {
                registerModal.hide();
                console.log('✅ Register modal hidden');
            }
        }

        // Show login modal after delay
        setTimeout(() => {
            this.showLoginModal();
        }, 300);
    }

    async handleLogin() {
        console.log('🔧 handleLogin() called');

        const username = document.getElementById('loginUsername')?.value;
        const password = document.getElementById('loginPassword')?.value;

        console.log('🔧 Username:', username);
        console.log('🔧 Password:', password ? 'PROVIDED' : 'MISSING');

        if (!username || !password) {
            console.error('❌ Missing username or password');
            this.showNotification('Please enter both username and password', 'warning');
            return;
        }

        try {
            const loginBtn = document.querySelector('#loginForm button[type="submit"]');
            if (loginBtn) {
                loginBtn.textContent = 'Logging in...';
                loginBtn.disabled = true;
            }

            console.log('🔐 Sending login request to API...');

            const response = await fetch('http://localhost:8081/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            console.log('📥 Login response status:', response.status);
            console.log('📥 Login response headers:', Object.fromEntries(response.headers.entries()));

            const responseText = await response.text();
            console.log('📥 Login response text:', responseText);

            if (!response.ok) {
                let errorMessage = 'Login failed';
                try {
                    const errorJson = JSON.parse(responseText);
                    errorMessage = errorJson.message || errorJson.error || errorMessage;
                    console.error('❌ Login error JSON:', errorJson);
                } catch {
                    console.error('❌ Login error text:', responseText);
                    if (responseText) {
                        errorMessage = responseText;
                    }
                }
                throw new Error(errorMessage);
            }

            let result;
            try {
                result = JSON.parse(responseText);
                console.log('✅ Login parsed result:', result);
            } catch (e) {
                console.error('❌ Failed to parse login response:', responseText);
                throw new Error('Invalid response from server');
            }

            // Check if we have a token
            if (!result.accessToken) {
                console.error('❌ No accessToken in response:', result);
                throw new Error('No authentication token received');
            }

            console.log('🔑 Token received, parts:', result.accessToken.split('.').length);
            console.log('👤 User role:', result.role);

            // Store authentication
            this.api.setToken(result.accessToken, result.refreshToken);
            this.currentUser = result;
            localStorage.setItem('user', JSON.stringify(this.currentUser));

            console.log('💾 User data stored in localStorage');
            console.log('💾 User:', this.currentUser);

            // Clear form
            document.getElementById('loginUsername').value = '';
            document.getElementById('loginPassword').value = '';

            // Close modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('loginModal'));
            if (modal) {
                modal.hide();
                console.log('✅ Login modal closed');
            }

            // Update UI
            this.updateUIAfterLogin();

            // Show success message
            this.showNotification('Login successful! Welcome back, ' + result.username, 'success');

            // Determine dashboard URL
            const dashboardUrl = this.getDashboardUrl(result.role || 'viewer');
            console.log('🎯 Dashboard URL:', dashboardUrl);
            console.log('🎯 Redirecting in 1 second...');

            // Redirect after 1 second
            setTimeout(() => {
                console.log('🔄 NOW REDIRECTING TO:', dashboardUrl);
                window.location.href = dashboardUrl;
            }, 1000);

        } catch (error) {
            console.error('❌ Login error:', error);
            console.error('❌ Error stack:', error.stack);
            this.showNotification(error.message || 'Login failed. Please check your credentials.', 'danger');
        } finally {
            const loginBtn = document.querySelector('#loginForm button[type="submit"]');
            if (loginBtn) {
                loginBtn.textContent = 'Login';
                loginBtn.disabled = false;
            }
        }
    }

    getDashboardUrl(role) {
        const roleLower = role.toLowerCase();
        console.log('🔧 Getting dashboard for role:', roleLower);

        if (roleLower === 'admin') return '/admin-dashboard.html';
        if (roleLower === 'moderator') return '/moderator-dashboard.html';
        if (roleLower === 'contributor') return '/contributor-dashboard.html';
        return '/dashboard.html';
    }

    async handleRegister() {
        console.log('🔧 handleRegister() called');

        const username = document.getElementById('registerUsername')?.value;
        const email = document.getElementById('registerEmail')?.value;
        const password = document.getElementById('registerPassword')?.value;
        const confirmPassword = document.getElementById('registerConfirmPassword')?.value;

        console.log('🔧 Registration data:', { username, email, password: password ? 'PROVIDED' : 'MISSING', confirmPassword: confirmPassword ? 'PROVIDED' : 'MISSING' });

        if (!username || !email || !password || !confirmPassword) {
            this.showNotification('Please fill in all required fields', 'warning');
            return;
        }

        if (password !== confirmPassword) {
            this.showNotification('Passwords do not match', 'warning');
            return;
        }

        if (password.length < 6) {
            this.showNotification('Password must be at least 6 characters', 'warning');
            return;
        }

        try {
            const registerBtn = document.querySelector('#registerForm button[type="submit"]');
            if (registerBtn) {
                registerBtn.textContent = 'Creating account...';
                registerBtn.disabled = true;
            }

            const registerData = {
                username,
                email,
                password,
                passwordConfirm: confirmPassword,
                firstName: document.getElementById('registerFirstName')?.value || '',
                lastName: document.getElementById('registerLastName')?.value || ''
            };

            console.log('📝 Sending registration request...', registerData);

            const response = await fetch('http://localhost:8081/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(registerData)
            });

            const responseText = await response.text();
            console.log('📥 Registration response status:', response.status);
            console.log('📥 Registration response:', responseText);

            if (!response.ok) {
                let errorMessage = 'Registration failed';
                try {
                    const errorJson = JSON.parse(responseText);
                    errorMessage = errorJson.message || errorJson.error || errorMessage;
                } catch {
                    if (responseText) {
                        errorMessage = responseText;
                    }
                }
                throw new Error(errorMessage);
            }

            let result;
            try {
                result = JSON.parse(responseText);
            } catch (e) {
                throw new Error('Invalid response from server');
            }

            // Clear form
            document.getElementById('registerUsername').value = '';
            document.getElementById('registerEmail').value = '';
            document.getElementById('registerPassword').value = '';
            document.getElementById('registerConfirmPassword').value = '';
            document.getElementById('registerFirstName').value = '';
            document.getElementById('registerLastName').value = '';

            // Close modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('registerModal'));
            if (modal) {
                modal.hide();
            }

            this.showNotification(result.message || 'Registration successful! Please check your email to verify your account.', 'success');

            // Auto switch to login modal after 2 seconds
            setTimeout(() => {
                this.switchToLogin();
            }, 2000);

        } catch (error) {
            console.error('❌ Registration error:', error);
            this.showNotification(error.message || 'Registration failed. Please try again.', 'danger');
        } finally {
            const registerBtn = document.querySelector('#registerForm button[type="submit"]');
            if (registerBtn) {
                registerBtn.textContent = 'Register';
                registerBtn.disabled = false;
            }
        }
    }

    updateUIAfterLogin() {
        console.log('🔧 Updating UI after login...');

        // Hide login button
        const loginBtn = document.getElementById('loginBtn');
        if (loginBtn) {
            loginBtn.style.display = 'none';
            console.log('✅ Login button hidden');
        }

        // Create user dropdown
        this.createUserDropdown();
    }

    createUserDropdown() {
        console.log('🔧 Creating user dropdown...');

        // Check if dropdown already exists
        if (document.getElementById('userDropdownContainer')) {
            console.log('✅ User dropdown already exists');
            return;
        }

        const user = this.currentUser;
        if (!user) {
            console.error('❌ No user data for dropdown');
            return;
        }

        console.log('🔧 Creating dropdown for user:', user.username);

        // Create dropdown container
        const container = document.createElement('div');
        container.id = 'userDropdownContainer';
        container.className = 'nav-item dropdown';

        // Create dropdown HTML
        container.innerHTML = `
            <a class="nav-link dropdown-toggle text-white" href="#" role="button" data-bs-toggle="dropdown">
                <i class="bi bi-person-circle me-1"></i>
                ${user.username}
            </a>
            <ul class="dropdown-menu dropdown-menu-end">
                <li>
                    <div class="dropdown-item-text">
                        <strong>${user.username}</strong><br>
                        <small class="text-muted">${user.role}</small>
                    </div>
                </li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" href="/dashboard.html">
                    <i class="bi bi-speedometer2 me-2"></i>Dashboard
                </a></li>
                <li><a class="dropdown-item" href="/profile-settings.html">
                    <i class="bi bi-person-gear me-2"></i>Profile
                </a></li>
                ${user.role === 'admin' || user.role === 'moderator' ? `
                <li><a class="dropdown-item text-warning" href="/${user.role}-dashboard.html">
                    <i class="bi bi-shield-check me-2"></i>${user.role === 'admin' ? 'Admin Panel' : 'Moderation'}
                </a></li>
                ` : ''}
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item text-danger" href="#" id="logoutBtn">
                    <i class="bi bi-box-arrow-right me-2"></i>Logout
                </a></li>
            </ul>
        `;

        // Add to navbar - try different locations
        let added = false;

        // Try navbar-nav first
        const navbarNav = document.querySelector('.navbar-nav.me-auto');
        if (navbarNav) {
            navbarNav.appendChild(container);
            added = true;
            console.log('✅ Dropdown added to navbar-nav');
        } else {
            // Try the search button container
            const searchContainer = document.querySelector('.d-flex');
            if (searchContainer && searchContainer.parentNode) {
                searchContainer.parentNode.insertBefore(container, searchContainer);
                added = true;
                console.log('✅ Dropdown added before search container');
            }
        }

        if (!added) {
            console.error('❌ Could not find place to add dropdown');
            return;
        }

        // Add logout event listener
        setTimeout(() => {
            const logoutBtn = document.getElementById('logoutBtn');
            if (logoutBtn) {
                logoutBtn.addEventListener('click', (e) => {
                    e.preventDefault();
                    console.log('🔧 Logout button clicked');
                    this.handleLogout();
                });
                console.log('✅ Logout button listener added');
            }
        }, 100);

        console.log('✅ User dropdown created');
    }

    handleLogout() {
        console.log('🔧 Handling logout...');

        localStorage.removeItem('user');
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        this.currentUser = null;
        this.api.setToken(null, null);

        console.log('✅ LocalStorage cleared');

        // Remove dropdown
        const dropdownContainer = document.getElementById('userDropdownContainer');
        if (dropdownContainer) {
            dropdownContainer.remove();
            console.log('✅ Dropdown removed');
        }

        // Show login button
        const loginBtn = document.getElementById('loginBtn');
        if (loginBtn) {
            loginBtn.style.display = 'block';
            console.log('✅ Login button shown');
        }

        this.showNotification('Logged out successfully', 'info');

        // Reload page after 1 second
        console.log('🔄 Reloading page in 1 second...');
        setTimeout(() => {
            window.location.reload();
        }, 1000);
    }

    checkAuthStatus() {
        console.log('🔧 Checking auth status...');
        if (this.currentUser) {
            console.log('✅ User is logged in:', this.currentUser.username);
            this.updateUIAfterLogin();
        } else {
            console.log('⚠️ No user logged in');
        }
    }

    showNotification(message, type = 'info') {
        console.log('🔧 Showing notification:', message);

        // Remove existing notifications
        const existingNotifications = document.querySelectorAll('.alert-dismissible');
        existingNotifications.forEach(notification => notification.remove());

        // Create notification
        const notification = document.createElement('div');
        notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
        notification.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;

        document.body.appendChild(notification);
        console.log('✅ Notification added');

        // Auto remove after 5 seconds
        setTimeout(() => {
            if (notification.parentNode) {
                notification.remove();
                console.log('✅ Notification removed');
            }
        }, 5000);
    }
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    console.log('🔧 DOM loaded, initializing AuthManager...');
    console.log('🔧 Bootstrap available:', typeof bootstrap !== 'undefined');
    console.log('🔧 Bootstrap Modal available:', typeof bootstrap?.Modal !== 'undefined');

    try {
        window.authManager = new AuthManager();
        console.log('✅ AuthManager initialized successfully');
    } catch (error) {
        console.error('❌ Failed to initialize AuthManager:', error);
        console.error('❌ Error stack:', error.stack);
    }
});

// Add test function to window
window.testAuth = function() {
    console.log('🧪 TEST: Testing authentication system');
    console.log('🧪 Current user:', JSON.parse(localStorage.getItem('user')));
    console.log('🧪 Current token:', localStorage.getItem('token'));
    console.log('🧪 AuthManager:', window.authManager);

    // Test API endpoint
    fetch('http://localhost:8081/api/health')
        .then(r => r.json())
        .then(data => console.log('🧪 API Health:', data))
        .catch(err => console.error('🧪 API Health check failed:', err));
};

console.log('✅ auth.js loaded successfully');