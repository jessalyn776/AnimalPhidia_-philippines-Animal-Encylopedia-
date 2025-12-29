// Authentication handling
class AuthManager {
    constructor() {
        this.api = apiClient;
        this.currentUser = JSON.parse(localStorage.getItem('user') || 'null');
        this.initEventListeners();
    }

    initEventListeners() {
        // Login button
        document.addEventListener('DOMContentLoaded', () => {
            const loginBtn = document.getElementById('loginBtn');
            if (loginBtn) {
                loginBtn.addEventListener('click', () => this.showLoginModal());
            }

            // Login form submission
            const loginForm = document.getElementById('loginForm');
            if (loginForm) {
                loginForm.addEventListener('submit', (e) => {
                    e.preventDefault();
                    this.handleLogin();
                });
            }

            // Register form submission
            const registerForm = document.getElementById('registerForm');
            if (registerForm) {
                registerForm.addEventListener('submit', (e) => {
                    e.preventDefault();
                    this.handleRegister();
                });
            }

            // Switch between login/register modals
            const showRegister = document.getElementById('showRegister');
            if (showRegister) {
                showRegister.addEventListener('click', (e) => {
                    e.preventDefault();
                    this.showRegisterModal();
                });
            }

            const showLogin = document.getElementById('showLogin');
            if (showLogin) {
                showLogin.addEventListener('click', (e) => {
                    e.preventDefault();
                    this.showLoginModal();
                });
            }

            // Check if user is logged in on page load
            this.checkAuthStatus();
        });
    }

    showLoginModal() {
        const loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
        loginModal.show();
    }

    showRegisterModal() {
        const registerModal = new bootstrap.Modal(document.getElementById('registerModal'));
        registerModal.show();
    }

    async handleLogin() {
        const username = document.getElementById('loginUsername').value;
        const password = document.getElementById('loginPassword').value;

        try {
            const loginBtn = document.querySelector('#loginForm button[type="submit"]');
            const originalText = loginBtn.textContent;
            loginBtn.textContent = 'Logging in...';
            loginBtn.disabled = true;

            const result = await this.api.login(username, password);

            // Close modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('loginModal'));
            modal.hide();

            // Update UI
            this.currentUser = result;
            this.updateUIAfterLogin();

            // Show success message
            this.showNotification('Login successful!', 'success');

            // Redirect based on role
            setTimeout(() => {
                this.redirectToDashboard(result.role);
            }, 1000);

        } catch (error) {
            this.showNotification(error.message || 'Login failed. Please check your credentials.', 'danger');
        } finally {
            const loginBtn = document.querySelector('#loginForm button[type="submit"]');
            if (loginBtn) {
                loginBtn.textContent = 'Login';
                loginBtn.disabled = false;
            }
        }
    }

    async handleRegister() {
        const userData = {
            username: document.getElementById('registerUsername').value,
            email: document.getElementById('registerEmail').value,
            password: document.getElementById('registerPassword').value,
            passwordConfirm: document.getElementById('registerConfirmPassword').value,
            firstName: document.getElementById('registerFirstName').value,
            lastName: document.getElementById('registerLastName').value
        };

        // Validate passwords match
        if (userData.password !== userData.passwordConfirm) {
            this.showNotification('Passwords do not match!', 'danger');
            return;
        }

        try {
            const registerBtn = document.querySelector('#registerForm button[type="submit"]');
            const originalText = registerBtn.textContent;
            registerBtn.textContent = 'Registering...';
            registerBtn.disabled = true;

            const result = await this.api.register(userData);

            // Close modal
            const modal = bootstrap.Modal.getInstance(document.getElementById('registerModal'));
            modal.hide();

            // Show success message
            this.showNotification('Registration successful! Please check your email to verify your account.', 'success');

            // Show login modal
            setTimeout(() => {
                this.showLoginModal();
            }, 2000);

        } catch (error) {
            this.showNotification(error.message || 'Registration failed. Please try again.', 'danger');
        } finally {
            const registerBtn = document.querySelector('#registerForm button[type="submit"]');
            if (registerBtn) {
                registerBtn.textContent = 'Register';
                registerBtn.disabled = false;
            }
        }
    }

    checkAuthStatus() {
        if (this.currentUser && this.api.token) {
            this.updateUIAfterLogin();
        }
    }

    updateUIAfterLogin() {
        const loginBtn = document.getElementById('loginBtn');
        if (loginBtn && this.currentUser) {
            loginBtn.innerHTML = `
                <i class="bi bi-person-circle me-1"></i>
                ${this.currentUser.username}
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="#" id="goToDashboard">
                        <i class="bi bi-speedometer2 me-2"></i>Dashboard
                    </a>
                    <a class="dropdown-item" href="#" id="logoutBtn">
                        <i class="bi bi-box-arrow-right me-2"></i>Logout
                    </a>
                </div>
            `;
            loginBtn.classList.add('dropdown-toggle');
            loginBtn.setAttribute('data-bs-toggle', 'dropdown');

            // Add logout handler
            setTimeout(() => {
                const logoutBtn = document.getElementById('logoutBtn');
                if (logoutBtn) {
                    logoutBtn.addEventListener('click', (e) => {
                        e.preventDefault();
                        this.logout();
                    });
                }

                const goToDashboard = document.getElementById('goToDashboard');
                if (goToDashboard) {
                    goToDashboard.addEventListener('click', (e) => {
                        e.preventDefault();
                        this.redirectToDashboard(this.currentUser.role);
                    });
                }
            }, 100);
        }
    }

    redirectToDashboard(role) {
        switch(role.toLowerCase()) {
            case 'admin':
                window.location.href = 'admin-dashbooard.html';
                break;
            case 'moderator':
                window.location.href = 'moderator-dashboard.html';
                break;
            case 'contributor':
            case 'viewer':
                window.location.href = 'dashboard-contributor.html';
                break;
            default:
                window.location.href = 'dashboard-contributor.html';
        }
    }

    logout() {
        this.api.logout();
        this.currentUser = null;
        this.showNotification('Logged out successfully', 'info');

        // Update UI
        const loginBtn = document.getElementById('loginBtn');
        if (loginBtn) {
            loginBtn.textContent = 'Login';
            loginBtn.classList.remove('dropdown-toggle');
            loginBtn.removeAttribute('data-bs-toggle');
            loginBtn.removeAttribute('aria-expanded');
        }
    }

    showNotification(message, type = 'info') {
        // Create notification element
        const alert = document.createElement('div');
        alert.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        alert.style.cssText = `
            top: 20px;
            right: 20px;
            z-index: 9999;
            min-width: 300px;
        `;
        alert.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;

        document.body.appendChild(alert);

        // Auto remove after 5 seconds
        setTimeout(() => {
            if (alert.parentNode) {
                alert.parentNode.removeChild(alert);
            }
        }, 5000);
    }

    getCurrentUser() {
        return this.currentUser;
    }

    isLoggedIn() {
        return !!this.currentUser && !!this.api.token;
    }
}

// Initialize auth manager
const authManager = new AuthManager();