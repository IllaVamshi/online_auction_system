# Bid Market – Online Auction System

A full-stack web application for creating and participating in online auctions, built with a Spring Boot backend and a vanilla JavaScript frontend.


## Project Structure
```text
online-auction/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/auction/onlineauction/
│   │   │       ├── config/                 # Security & JWT configuration
│   │   │       ├── controller/             # REST API endpoints
│   │   │       ├── dto/                    # Data transfer objects
│   │   │       ├── entity/                 # JPA entities
│   │   │       ├── repository/             # Database layer
│   │   │       └── service/                # Business logic & scheduler
│   │   └── resources/
│   │       ├── application.properties     # App + DB + JWT configuration
│   │       └── static/                    # Frontend (vanilla JS SPA-style)
│   │           ├── index.html             # Public home landing page
│   │           ├── login.html             # Login page
│   │           ├── register.html          # Register page
│   │           ├── dashboard.html         # User dashboard
│   │           ├── auctions.html          # All auctions
│   │           ├── my-auctions.html       # User auctions
│   │           ├── my-bids.html           # User bids
│   │           ├── create-auction.html    # Create auction
│   │           ├── auction-detail.html    # Auction details & bidding
│   │           ├── profile.html           # User profile
│   │           ├── css/style.css          # Styles
│   │           └── js/app.js              # Shared JavaScript
│   └── test/
│       └── java/                         # Test classes
├── pom.xml                               # Maven configuration
├── mvnw & mvnw.cmd                       # Maven wrapper
└── README.md                             # Project documentation
```


## Backend

The backend is built with Spring Boot 3.x, using:

- Spring Web: REST API for auctions, bids, authentication, and profile.
- Spring Security + JWT: Stateless authentication using Bearer tokens.
- Spring Data JPA: Persistence layer.
- MySQL (or similar SQL DB): Main database (configured via application.properties).
- Lombok: To reduce boilerplate (getters/setters, builders, etc.).
- Spring Scheduling: For automatically closing auctions after their end time.


## Key Components

### AuthController

- POST `/api/auth/register` – Register a new user
- POST `/api/auth/login` – Authenticate and return JWT token

### AuctionController

- GET `/api/auctions` – List all auctions (active + closed, active first)
- GET `/api/auctions/active` – List only active auctions
- GET `/api/auctions/{id}` – Get auction details
- GET `/api/auctions/{id}/bids` – Get auction bids
- GET `/api/auctions/{id}/highest-bid` – Get highest bid
- GET `/api/auctions/my` – Get user auctions
- GET `/api/auctions/dashboard/stats` – Dashboard metrics
- POST `/api/auctions` – Create auction
- PUT `/api/auctions/{id}` – Update auction (owner only)
- DELETE `/api/auctions/{id}` – Delete auction (owner only)

### BidController

- POST `/api/bids/place` – Place bid
- GET `/api/bids/auction/{auctionId}` – List bids
- GET `/api/bids/auction/{auctionId}/highest` – Highest bid
- GET `/api/bids/my` – User bids

### UserController

- GET `/api/users/profile` – Get profile
- PUT `/api/users/profile` – Update profile name
- POST `/api/users/change-password` – Change password

### AuctionScheduler

- Runs periodically (every minute)
- Finds expired auctions (endTime passed and not closed)
- Determines highest bid
- Sets winner if bids exist
- Marks auction as closed
- Saves updated auctions



## Configuration

- Database configured in `application.properties`
- MySQL schema: `auction_system`

### JWT

- Secret: `app.jwt.secret`
- Expiration: `app.jwt.expiration-ms`

### Application

- Runs on port `8080` (default)
- JPA: `spring.jpa.hibernate.ddl-auto=update`

### Uploads

- Directory: `uploads`
- Served via `/uploads/**`

### Security

- Public endpoints:
  - `/api/auth/**`, `/`, static files
- Protected endpoints:
  - Auctions, bids, profile, password
- CORS enabled for localhost




## Frontend

The frontend is a single-page-like application using vanilla JavaScript, HTML, and CSS, served from Spring Boot static resources.


3## Features

- Dashboard: Displays total auctions, active auctions, and user bids  
- Auctions Listing: View all auctions (active and closed)  
- Create Auction: Form to create new auctions with optional image upload  
- Bidding System: Place bids and view bid history  
- User Profile: Update profile and change password  
- Authentication: Login and register with JWT-based authentication  
- Navigation: Dynamic navbar and sidebar based on login state  


3## Technologies

- HTML5: Structure  
- CSS3: Styling and responsive design  
- JavaScript (ES6+): UI logic and API integration  


3## Key Files

- `index.html`: Landing page  
- `login.html`: Login page  
- `register.html`: Registration page  
- `dashboard.html`: User dashboard  
- `auctions.html`: Auctions listing  
- `create-auction.html`: Create auction form  
- `auction-detail.html`: Auction details and bidding  
- `profile.html`: User profile  
- `app.js`: Handles UI logic, API calls, and authentication  
- `style.css`: Styling  




## Getting Started

### 1. Prerequisites

- Java 21+
- Maven (or mvnw wrapper)
- MySQL (or your configured SQL DB)


### 2. Run the Backend

- ./mvnw spring-boot:run

  or

- mvn spring-boot:run

  The application will start on the configured port (e.g. http://localhost:8080).


### 3. Access the UI

- Open http://localhost:8080 in your browser  

- From the Home page (Bid Market landing), you can:
  - Register a new account  
  - Log in and access the dashboard, auctions, etc.  


### 4. API Testing

- Use a REST client (Postman, VS Code REST Client, etc.)  
- Test endpoints under `/api/...`




## Notes

- CORS is configured in SecurityConfig to allow localhost frontends.
- Static assets (HTML/CSS/JS) are served directly by Spring Boot from src/main/resources/static.
- Auction closing and winner determination are handled automatically by the scheduled AuctionScheduler.
- Password change and validation errors return clear JSON messages, used by the frontend to show friendly error alerts.

## References of the site

### Home Page
![Home](References%20of%20the%20site/home.png)


### Register Page
![Register](References%20of%20the%20site/register.png)


### Login Page
![Login](References%20of%20the%20site/login.png)


### Dashboard
![Dashboard](References%20of%20the%20site/dashboard.png)


### Create Auction
![Create Auction](References%20of%20the%20site/create%20auction.png)


### Auction Details
![Auction Details](References%20of%20the%20site/auction%20details.png)


### My Bids
![My Bids](References%20of%20the%20site/my%20bids.png)


### Sidebar
![Sidebar](References%20of%20the%20site/sidebar.png)


### Profile
![Profile](References%20of%20the%20site/profile.png)

