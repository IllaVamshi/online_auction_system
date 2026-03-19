package com.auction.onlineauction.controller;

import com.auction.onlineauction.config.SecurityUtils;
import com.auction.onlineauction.dto.AuctionDto;
import com.auction.onlineauction.dto.BidDto;
import com.auction.onlineauction.entity.Auction;
import com.auction.onlineauction.entity.User;
import com.auction.onlineauction.service.AuctionService;
import com.auction.onlineauction.service.BidService;
import com.auction.onlineauction.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Compatibility layer for frontends that use /api/items instead of /api/auctions.
 * Delegates to existing AuctionService/BidService.
 */
@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final AuctionService auctionService;
    private final BidService bidService;
    private final UserService userService;

    public ItemController(AuctionService auctionService, BidService bidService, UserService userService) {
        this.auctionService = auctionService;
        this.bidService = bidService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<AuctionDto>> listItems() {
        List<AuctionDto> auctions = auctionService.getAllAuctions().stream()
                .map(auctionService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(auctions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionDto> getItem(@PathVariable Long id) {
        Auction auction = auctionService.getAuction(id);
        return ResponseEntity.ok(auctionService.toDto(auction));
    }

    @GetMapping("/{id}/bids")
    public ResponseEntity<List<BidDto>> getItemBids(@PathVariable Long id) {
        List<BidDto> bids = bidService.getBidsForAuction(id).stream()
                .map(bidService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/{id}/highest-bid")
    public ResponseEntity<BidDto> getItemHighestBid(@PathVariable Long id) {
        return ResponseEntity.ok(bidService.toDto(bidService.getHighestBid(id)));
    }

    @GetMapping("/my-auctions")
    public ResponseEntity<List<AuctionDto>> myItems() {
        String email = SecurityUtils.getCurrentUserEmail().orElseThrow(() -> new IllegalArgumentException("Unauthenticated"));
        User user = userService.findByEmail(email);
        List<AuctionDto> auctions = user.getAuctions().stream()
                .map(auctionService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(auctions);
    }
}

