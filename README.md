# Bid Market – Online Auction System

A full-stack web application for creating and participating in online auctions, built with a Spring Boot backend and a vanilla JavaScript frontend.
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
│   │           ├── index.html             # Home page
│   │           ├── login.html             # Login
│   │           ├── register.html          # Register
│   │           ├── dashboard.html         # Dashboard
│   │           ├── auctions.html          # All auctions
│   │           ├── my-auctions.html       # User auctions
│   │           ├──
