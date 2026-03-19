package com.auction.onlineauction.controller;

import com.auction.onlineauction.service.UserService;
import com.auction.onlineauction.service.AuctionService;
import com.auction.onlineauction.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private BidService bidService;

    @PostMapping("/clear-data")
    public ResponseEntity<?> clearAllData() {
        try {
            // Delete in order to avoid foreign key constraints
            bidService.deleteAllBids();
            auctionService.deleteAllAuctions();
            userService.deleteAllUsers();

            return ResponseEntity.ok(java.util.Map.of("message", "All data cleared successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(java.util.Map.of("error", "Error clearing data: " + e.getMessage()));
        }
    }
}