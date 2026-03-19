package com.auction.onlineauction.service;

import com.auction.onlineauction.dto.BidDto;
import com.auction.onlineauction.entity.Auction;
import com.auction.onlineauction.entity.Bid;
import com.auction.onlineauction.entity.User;
import com.auction.onlineauction.repository.AuctionRepository;
import com.auction.onlineauction.repository.BidRepository;
import com.auction.onlineauction.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BidServiceImpl implements BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public BidServiceImpl(BidRepository bidRepository,
                          AuctionRepository auctionRepository,
                          UserRepository userRepository) {
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Bid placeBid(Long bidderId, Long auctionId, BigDecimal amount) {
        User bidder = userRepository.findById(bidderId)
                .orElseThrow(() -> new IllegalArgumentException("Bidder not found"));
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

        // Check if the bidder is the auction creator
        if (auction.getCreatedBy().getId().equals(bidderId)) {
            throw new IllegalArgumentException("You cannot bid on your own auction");
        }

        if (auction.isClosed() || auction.getEndTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Auction has already ended");
        }

        BigDecimal currentHighest = auction.getCurrentPrice() != null ? auction.getCurrentPrice() : auction.getStartingPrice();
        if (currentHighest == null) {
            currentHighest = BigDecimal.ZERO;
        }

        if (amount.compareTo(currentHighest) <= 0) {
            throw new IllegalArgumentException("Bid must be higher than current highest bid");
        }

        Bid bid = Bid.builder()
                .amount(amount)
                .auction(auction)
                .bidder(bidder)
                .bidTime(LocalDateTime.now())
                .build();

        Bid saved = bidRepository.save(bid);

        auction.setCurrentPrice(amount);
        auctionRepository.save(auction);

        return saved;
    }

    @Override
    public List<Bid> getBidsForAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));
        return bidRepository.findByAuctionOrderByAmountDesc(auction);
    }

    @Override
    public Bid getHighestBid(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

        Bid highestBid = bidRepository.findFirstByAuctionWithBidderOrderByAmountDesc(auction).orElse(null);
        
        if (highestBid != null) {
            System.out.println("Highest bid for auction " + auctionId + ": " + highestBid.getAmount() + " by " + highestBid.getBidder().getName());
        } else {
            System.out.println("No bids found for auction " + auctionId);
        }
        
        return highestBid;
    }

    @Override
    public BidDto toDto(Bid bid) {
        if (bid == null) {
            return null;
        }
        BidDto dto = new BidDto();
        dto.setId(bid.getId());
        dto.setAmount(bid.getAmount());
        dto.setBidTime(bid.getBidTime());
        dto.setAuctionId(bid.getAuction() != null ? bid.getAuction().getId() : null);
        dto.setAuctionTitle(bid.getAuction() != null ? bid.getAuction().getTitle() : null);
        dto.setBidderId(bid.getBidder() != null ? bid.getBidder().getId() : null);
        dto.setBidderName(bid.getBidder() != null ? bid.getBidder().getName() : null);
        return dto;
    }

    @Override
    @Transactional
    public void deleteAllBids() {
        bidRepository.deleteAll();
    }
}
