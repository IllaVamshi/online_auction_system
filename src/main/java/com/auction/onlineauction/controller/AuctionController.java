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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auctions")
public class AuctionController {

    private final AuctionService auctionService;
    private final BidService bidService;
    private final UserService userService;

    public AuctionController(AuctionService auctionService, BidService bidService, UserService userService) {
        this.auctionService = auctionService;
        this.bidService = bidService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<AuctionDto>> listAllAuctions() {
        // All auctions (active + closed), active first
        List<AuctionDto> auctions = auctionService.getAllAuctionsIncludingClosed().stream()
                .sorted(Comparator
                        .comparing(Auction::isClosed)              // false (active) before true (closed)
                        .thenComparing(Auction::getEndTime))       // then by end time
                .map(auctionService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(auctions);
    }

    @GetMapping("/active")
    public ResponseEntity<List<AuctionDto>> listActiveAuctions() {
        List<AuctionDto> auctions = auctionService.getAllAuctions().stream()
                .map(auctionService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(auctions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionDto> getAuction(@PathVariable Long id) {
        Auction auction = auctionService.getAuction(id);
        return ResponseEntity.ok(auctionService.toDto(auction));
    }

    @GetMapping("/{id}/bids")
    public ResponseEntity<List<BidDto>> getAuctionBids(@PathVariable Long id) {
        List<BidDto> bids = bidService.getBidsForAuction(id).stream()
                .map(bidService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/{id}/highest-bid")
    public ResponseEntity<BidDto> getHighestBid(@PathVariable Long id) {
        return ResponseEntity.ok(bidService.toDto(bidService.getHighestBid(id)));
    }

    @PostMapping
    public ResponseEntity<AuctionDto> createAuction(@RequestBody AuctionDto auctionDto) {
        String email = SecurityUtils.getCurrentUserEmail().orElseThrow(() -> new IllegalArgumentException("Unauthenticated"));
        User user = userService.findByEmail(email);
        Auction auction = auctionService.createAuction(auctionDto, user.getId());
        return ResponseEntity.ok(auctionService.toDto(auction));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuctionDto> updateAuction(@PathVariable Long id, @RequestBody AuctionDto auctionDto) {
        String email = SecurityUtils.getCurrentUserEmail().orElseThrow(() -> new IllegalArgumentException("Unauthenticated"));
        User user = userService.findByEmail(email);
        Auction updated = auctionService.updateAuction(id, auctionDto, user.getId());
        return ResponseEntity.ok(auctionService.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuction(@PathVariable Long id) {
        String email = SecurityUtils.getCurrentUserEmail().orElseThrow(() -> new IllegalArgumentException("Unauthenticated"));
        User user = userService.findByEmail(email);
        auctionService.deleteAuction(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<AuctionDto>> getMyAuctions() {
        String email = SecurityUtils.getCurrentUserEmail().orElseThrow(() -> new IllegalArgumentException("Unauthenticated"));
        User user = userService.findByEmail(email);
        List<AuctionDto> auctions = user.getAuctions().stream()
                .map(auctionService::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(auctions);
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getDashboardStats() {
        List<Auction> allAuctions = auctionService.getAllAuctionsIncludingClosed();
        List<Auction> activeAuctions = auctionService.getAllAuctions();
        
        return ResponseEntity.ok()
            .header("Cache-Control", "no-cache, no-store, must-revalidate")
            .body(Map.of(
                "totalAuctions", allAuctions.size(),
                "activeAuctions", activeAuctions.size()
            ));
    }
}
