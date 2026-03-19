const API_BASE = "/api";

function getToken() {
    return localStorage.getItem("auction_token");
}

function setToken(token) {
    if (token) {
        localStorage.setItem("auction_token", token);
    } else {
        localStorage.removeItem("auction_token");
    }
}

function isLoggedIn() {
    return !!getToken();
}

function apiRequest(path, options = {}) {
    const token = getToken();
    const headers = options.headers || {};
    const isFormData = typeof FormData !== "undefined" && options.body instanceof FormData;
    if (!isFormData) {
        headers["Content-Type"] = headers["Content-Type"] || "application/json";
    }
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }
    return fetch(API_BASE + path, { ...options, headers })
        .then(async (response) => {
            if (!response.ok) {
                let errorMessage = response.statusText || `Request failed with status ${response.status}`;
                const contentType = response.headers.get('content-type');
                if (contentType && contentType.includes('application/json')) {
                    try {
                        const errorData = await response.json();
                        errorMessage = errorData.error || errorData.message || errorMessage;
                    } catch {
                        // If JSON parsing fails, fall back to status text
                    }
                } else {
                    try {
                        const text = await response.text();
                        if (text && text.length < 500) { // Limit to avoid large HTML
                            errorMessage = text;
                        }
                    } catch {
                        // If text reading fails, use status text
                    }
                }
                if (!errorMessage || errorMessage.trim() === '') {
                    errorMessage = 'An error occurred. Please try again.';
                }
                // Do not auto-clear JWT on any random 401/403 call.
                // Let the UI decide what to do (show error, redirect to login, etc.).
                throw new Error(errorMessage);
            }
            if (response.status === 204) {
                return null;
            }
            return response.json();
        });
}

function createNav() {
    const nav = document.getElementById("main-nav");
    if (!nav) {
        return;
    }
    const token = getToken();
    let html = `
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
            <div class="container">
                <a class="navbar-brand" href="/">Bid Market</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navContent">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse justify-content-end" id="navContent">
                    <ul class="navbar-nav">
    `;
    if (!token) {
        html += `
            <li class="nav-item"><a class="nav-link" href="/">Home</a></li>
        `;
        html += `
            <li class="nav-item"><a class="nav-link" href="/login.html">Login</a></li>
            <li class="nav-item"><a class="nav-link" href="/register.html">Register</a></li>
        `;
    } else {
        html += `
            <li class="nav-item"><a class="nav-link" href="/dashboard.html">Dashboard</a></li>
            <li class="nav-item"><a class="nav-link" href="/create-auction.html">Create Auction</a></li>
            <li class="nav-item"><a class="nav-link" href="/auctions.html">Auctions</a></li>
            <li class="nav-item"><a class="nav-link" href="/my-auctions.html">My Auctions</a></li>
            <li class="nav-item"><a class="nav-link" href="/my-bids.html">My Bids</a></li>
            <li class="nav-item"><a class="nav-link" href="#" id="logoutLink">Logout</a></li>
        `;
    }
    html += `
                    </ul>
                    ${token ? `
                    <button class="btn btn-primary ms-3 d-flex align-items-center justify-content-center" id="navSidebarToggle" type="button" aria-label="Toggle sidebar">
                        <span class="navbar-toggler-icon"></span>
                    </button>` : ``}
                </div>
            </div>
        </nav>
    `;
    nav.innerHTML = html;
    const logoutLink = document.getElementById("logoutLink");
    if (logoutLink) {
        logoutLink.addEventListener("click", (e) => {
            e.preventDefault();
            setToken(null);
            window.location.href = "/";
        });
    }

    // Initialize sidebar elements (only when logged in, created once per page)
    if (token && !document.getElementById("sidebar")) {
        const sidebar = document.createElement("div");
        sidebar.id = "sidebar";
        sidebar.className = "sidebar drawer-right bg-white text-dark";
        sidebar.innerHTML = `
            <div class="drawer-header d-flex align-items-center justify-content-between p-3 border-bottom">
              <div class="d-flex align-items-center">
                <img src="https://ui-avatars.com/api/?name=Online+Auction+User&background=2563eb&color=fff&size=48" class="rounded-circle me-2" width="48" height="48" alt="User Avatar">
                <div>
                  <div class="fw-bold">Bid Market User</div>
                </div>
              </div>
              <button class="drawer-close btn btn-link text-dark fs-4 p-0" id="drawerClose" aria-label="Close"><i class="fas fa-times"></i></button>
            </div>
            <nav class="drawer-nav p-3">
              <a href="/dashboard.html" class="drawer-link"><i class="fas fa-th-large me-2"></i>Dashboard</a>
              <a href="/create-auction.html" class="drawer-link"><i class="fas fa-pen-nib me-2"></i>Create Auction</a>
              <a href="/auctions.html" class="drawer-link"><i class="fas fa-list-alt me-2"></i>Auctions</a>
              <a href="/my-auctions.html" class="drawer-link"><i class="fas fa-dollar-sign me-2"></i>My Auctions</a>
              <a href="/my-bids.html" class="drawer-link"><i class="fas fa-bullseye me-2"></i>My Bids</a>
              <a href="/profile.html" class="drawer-link"><i class="fas fa-user-circle me-2"></i>Profile</a>
              <hr>
              <a href="#" id="sidebarLogoutLink" class="drawer-link text-danger"><i class="fas fa-sign-out-alt me-2"></i>Sign out</a>
            </nav>
        `;
        document.body.appendChild(sidebar);

        const overlay = document.createElement("div");
        overlay.id = "sidebarOverlay";
        overlay.className = "sidebar-overlay";
        document.body.appendChild(overlay);

        const sidebarToggle = document.getElementById("navSidebarToggle");
        const drawerClose = document.getElementById("drawerClose");

        function toggleSidebar() {
            sidebar.classList.toggle("show");
            overlay.classList.toggle("show");
        }

        if (sidebarToggle) {
            sidebarToggle.addEventListener("click", toggleSidebar);
        }
        overlay.addEventListener("click", toggleSidebar);
        if (drawerClose) {
            drawerClose.addEventListener("click", toggleSidebar);
        }

        document.querySelectorAll(".drawer-link").forEach(link => {
            link.addEventListener("click", () => {
                if (window.innerWidth <= 768) {
                    sidebar.classList.remove("show");
                    overlay.classList.remove("show");
                }
            });
        });

        const sidebarLogoutLink = document.getElementById("sidebarLogoutLink");
        if (sidebarLogoutLink) {
            sidebarLogoutLink.addEventListener("click", (e) => {
                e.preventDefault();
                setToken(null);
                window.location.href = "/";
            });
        }
    }
}

function formatCurrency(value) {
    return new Intl.NumberFormat(undefined, { style: "currency", currency: "USD" }).format(value);
}

function formatDateTime(value) {
    if (!value) return "-";
    const date = new Date(value);
    return date.toLocaleString();
}

function formatTimeRemaining(endTime) {
    if (!endTime) return "-";
    const now = new Date();
    const end = new Date(endTime);
    const diff = end - now;
    if (diff <= 0) return "Ended";
    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
    const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    if (days > 0) return `${days}d ${hours}h`;
    if (hours > 0) return `${hours}h ${minutes}m`;
    return `${minutes}m`;
}

window.addEventListener("DOMContentLoaded", createNav);

// Redirect logged-in users away from the public home page
window.addEventListener("DOMContentLoaded", () => {
    const token = getToken();
    if (token && window.location.pathname === "/") {
        window.location.replace("/dashboard.html");
    }
});
