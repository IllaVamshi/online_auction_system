import json
import urllib.request

# Change token to the one obtained from /api/auth/login
TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZWxsZXJAZXhhbXBsZS5jb20iLCJyb2xlIjoiUk9MRV9TRUxMRVIiLCJpYXQiOjE3NzM0MTEwNTIsImV4cCI6MTc3NDkxNzA1Mn0.vFkpIZM98gZunK6hhNtR5qC0H-MnF6OPQMzgWh74hsk"

body = {
    "title": "Painting",
    "description": "A beautiful painting",
    "startingPrice": 100.0,
    "auctionEndTime": "2026-12-31T23:59:59",
    "imageUrl": "https://via.placeholder.com/600"
}

req = urllib.request.Request(
    "http://localhost:8080/api/items",
    data=json.dumps(body).encode("utf-8"),
    headers={
        "Content-Type": "application/json",
        "Authorization": f"Bearer {TOKEN}"
    }
)
print(urllib.request.urlopen(req).read().decode())
