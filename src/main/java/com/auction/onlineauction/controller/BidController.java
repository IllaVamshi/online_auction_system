package com.auction.onlineauction.controller;

import com.auction.onlineauction.config.SecurityUtils;
import com.auction.onlineauction.dto.BidDto;
import com.auction.onlineauction.entity.Bid;
import com.auction.onlineauction.entity.User;
import com.auction.onlineauction.service.BidService;
import com.auction.onlineauction.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    private final BidService bidService;
    private final UserService userService;

    public BidController(BidService bidService, UserService userService) {
        this.bidService = bidService;
        this.userService = userService;
    }

    @PostMapping("/place")
    public ResponseEntity<?> placeBid(@RequestBody Map<String, Object> bidRequest) {
        try {
            Long auctionId = Long.valueOf(bidRequest.get("auctionId").toString());
            BigDecimal amount = new BigDecimal(bidRequest.get("amount").toString());
            
            String email = SecurityUtils.getCurrentUserEmail().orElseThrow(() -> new IllegalArgumentException("Unauthenticated"));
            User user = userService.findByEmail(email);
            Bid bid = bidService.placeBid(user.getId(), auctionId, amount);
            return ResponseEntity.ok(bidService.toDto(bid));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<BidDto>> getBids(@PathVariable Long auctionId) {
        List<BidDto> bids = bidService.getBidsForAuction(auctionId).stream()
                .map(bidService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/auction/{auctionId}/highest")
    public ResponseEntity<BidDto> getHighestBid(@PathVariable Long auctionId) {
        Bid bid = bidService.getHighestBid(auctionId);
        return ResponseEntity.ok(bidService.toDto(bid));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BidDto>> getMyBids() {
        String email = SecurityUtils.getCurrentUserEmail().orElseThrow(() -> new IllegalArgumentException("Unauthenticated"));
        User user = userService.findByEmail(email);
        List<BidDto> bids = user.getBids().stream()
                .map(bidService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bids);
    }
}
