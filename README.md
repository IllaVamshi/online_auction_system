# Bid Market – Online Auction System

A full-stack web application for creating and participating in online auctions, built with a Spring Boot backend and a vanilla JavaScript frontend.

---

## Project Structure
online-auction/
├── src/
│ ├── main/
│ │ ├── java/
│ │ │ └── com/auction/onlineauction/
│ │ │ ├── config/ # Security & JWT configuration
│ │ │ ├── controller/ # REST API endpoints
│ │ │ ├── dto/ # Data transfer objects
│ │ │ ├── entity/ # JPA entities
│ │ │ ├── repository/ # Database layer
│ │ │ └── service/ # Business logic & scheduler
│ │ └── resources/
│ │ ├── application.properties # App + DB + JWT configuration
│ │ └── static/ # Frontend (vanilla JS SPA-style)
│ │ ├── index.html # Public home landing page
│ │ ├── login.html # Login page
│ │ ├── register.html # Register page
│ │ ├── dashboard.html # User dashboard
│ │ ├── auctions.html # All auctions
│ │ ├── my-auctions.html # User auctions
│ │ ├── my-bids.html # User bids
│ │ ├── create-auction.html # Create auction
│ │ ├── auction-detail.html # Auction details & bidding
│ │ ├── profile.html # User profile
│ │ ├── css/style.css # Styles
│ │ └── js/app.js # Shared JavaScript
│ └── test/
│ └── java/ # Test classes
├── pom.xml # Maven configuration
├── mvnw & mvnw.cmd # Maven wrapper
└── README.md # Project documentation

---

## Backend

The backend is built with Spring Boot 3.x, using:

- Spring Web: REST API for auctions, bids, authentication, and profile.
- Spring Security + JWT: Stateless authentication using Bearer tokens.
- Spring Data JPA: Persistence layer.
- MySQL (or similar SQL DB): Main database (configured via application.properties).
- Lombok: To reduce boilerplate (getters/setters, builders, etc.).
- Spring Scheduling: For automatically closing auctions after their end time.

---

## Key Components

### AuthController

Authentication endpoints:

- POST /api/auth/register – Register a new user.
- POST /api/auth/login – Authenticate and return a JWT token.

---

### AuctionController

- GET /api/auctions – List all auctions (active + closed, active first).
- GET /api/auctions/active – List only active auctions (used by dashboard).
- GET /api/auctions/{id} – Get details for a specific auction.
- GET /api/auctions/{id}/bids – Get bids for an auction.
- GET /api/auctions/{id}/highest-bid – Get highest bid for an auction.
- GET /api/auctions/my – Get auctions created by the logged-in user.
- GET /api/auctions/dashboard/stats – Dashboard metrics (total vs active auctions).
- POST /api/auctions – Create a new auction (authenticated).
- PUT /api/auctions/{id} – Update an auction (owner only).
- DELETE /api/auctions/{id} – Delete an auction (owner only).

---

### BidController

- POST /api/bids/place – Place a bid on an auction.
- GET /api/bids/auction/{auctionId} – List bids for a given auction.
- GET /api/bids/auction/{auctionId}/highest – Highest bid for an auction.
- GET /api/bids/my – Bids placed by the current user.

---

### UserController

- GET /api/users/profile – Get current user profile.
- PUT /api/users/profile – Update profile name.
- POST /api/users/change-password – Change user password (validates current password and returns clear error messages).

---

### AuctionScheduler

- Runs periodically (e.g. every minute).
- Finds auctions whose endTime has passed and closed = false.
- Determines highest bid for each such auction:
  - Sets winner on the Auction entity when bids exist.
  - Marks the auction as closed so frontend shows it as “Closed”.
- Saves updated auctions.

---

## Configuration

- Database: Configured in application.properties (e.g. MySQL on auction_system schema).
- JWT:
  - Secret: app.jwt.secret
  - Expiration: app.jwt.expiration-ms
- Port: Default 8080 (or as configured in application.properties).
- JPA: spring.jpa.hibernate.ddl-auto=update (auto create/update tables in dev).

Uploads (optional feature):

- app.upload.dir=uploads – directory for image uploads, served under /uploads/**.

SecurityConfig sets:

- Public endpoints for:
  - /api/auth/**, /, static pages, CSS/JS, images, uploaded images.
- JWT-protected endpoints for auctions, bids, profile, and password change.
- CORS allowed for typical localhost dev origins.

---

## Domain Model

### User

- id, name, email, password, createdAt

One-to-many:
- auctions – auctions created by the user.
- bids – bids placed by the user.

---

### Auction

- id, title, description, startingPrice, currentPrice, endTime, imageUrl, closed

Many-to-one:
- createdBy (User)
- winner (User, nullable)

One-to-many:
- bids – list of bids.

---

### Bid

- id, amount, bidTime

Many-to-one:
- auction
- bidder (User)

DTOs (AuctionDto, BidDto, UserProfileDto) are used to shape responses for the frontend.

---

## Frontend

The frontend is a vanilla JavaScript single-page-like app served from src/main/resources/static.

---

### Shared JS (static/js/app.js)

Manages JWT token:

- Stores token in localStorage as auction_token.
- Adds Authorization: Bearer <token> to API calls when logged in.

apiRequest(path, options) helper wraps fetch and:

- Automatically sets headers.
- Handles JSON errors and surfaces meaningful messages.

Dynamically renders the top navbar based on auth state:

- When logged out: Home, Login, Register.
- When logged in: Dashboard, Create Auction, Auctions, My Auctions, My Bids, Logout.

Creates a right-side drawer sidebar when logged in with the same navigation options and a profile entry.

---

## Key Pages

### Home (index.html)

- Marketing-style hero section:
  - Tagline, gradient headline, CTA buttons (“Get Started Free”, “Sign In”).
- Short “Why us” section with three feature cards (easy bidding, secure, 24/7).
- Visible only when logged out (logged-in users are redirected to dashboard.html).

---

### Login / Register

- Centered card forms with icons and validation.
- Use apiRequest('/auth/login') and /auth/register.
- On successful login, store token then redirect to dashboard.html.

---

### Dashboard (dashboard.html)

- Compact metric cards:
  - Total Auctions, Active Auctions, My Bids.
- “Latest Auctions” grid:
  - Shows only active auctions, via GET /api/auctions/active.
- View Details links to auction-detail.html?id=....
- Responsive layout, no horizontal scroll.

---

### Auctions (auctions.html)

- Lists all auctions from GET /api/auctions.
- Active auctions are shown first; closed auctions below.

Each card shows:
- Status badge (Active or Closed).
- Title, description, current bid, remaining time / “Ended”.

For closed auctions:
- Winner name + winning amount, or “Winner: No bids placed”.

- View Details button.

---

### Create Auction

- Vertical form:
  - Title, starting price, description, end time.
- Optional image URL and/or image file upload.

Image upload:
- Shows a preview.
- If a file is chosen, it is POSTed to /api/uploads and the returned URL is stored in imageUrl.

- On success, redirects back to dashboard.

---

### My Auctions / My Bids

- My Auctions: All auctions where the current user is creator.
- My Bids: All bids placed by current user.

Both pages render cards/tables with key information and links to details.

---

### Auction Detail (auction-detail.html)

- Shows a single auction with image, title, description, prices, status, creator and winner info.

Bidding section (if auction is open and user is logged in):
- Enter bid amount and place bid via /api/bids/place.

Bids list:
- Shows history of bids with bidder names and timestamps.

When auction is ended:
- Displays winner name and winning bid; or “No bids placed”.

If the logged-in user is the winner:
- Shows a banner: “You won this auction. Congratulations!”.

---

### Profile (profile.html)

- Left card: Profile information (name editable, email/user ID/role read-only).
- Right card: Change password form.

Uses:
- GET /api/users/profile to load data.
- PUT /api/users/profile to update name.
- POST /api/users/change-password to update password.

If current password is wrong:
- Shows a friendly message (“Current password is incorrect”) from backend.

---

## Getting Started

### Prerequisites

- Java 21+
- Maven (or mvnw wrapper)
- MySQL (or your configured SQL DB)

---

### Run the backend
./mvnw spring-boot:run

or

mvn spring-boot:run

The application will start on the configured port (e.g. http://localhost:8080).

---

### Access the UI

Open http://localhost:8080 in your browser.

From the Home page (Bid Market landing), you can:
- Register a new account.
- Log in and access the dashboard, auctions, etc.

---

### API Testing

Use a REST client (Postman, VS Code REST Client, etc.) against endpoints under /api/....

Authenticate first via POST /api/auth/login to obtain a JWT token; send it as a Bearer token in the Authorization header.

---

## Notes

- CORS is configured in SecurityConfig to allow localhost frontends.
- Static assets (HTML/CSS/JS) are served directly by Spring Boot from src/main/resources/static.
- Auction closing and winner determination are handled automatically by the scheduled AuctionScheduler.
- Password change and validation errors return clear JSON messages, used by the frontend to show friendly error alerts.
