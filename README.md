# Bid Market – Online Auction System

A full-stack web application for creating and participating in online auctions, built with a Spring Boot backend and a vanilla JavaScript frontend.

---

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
