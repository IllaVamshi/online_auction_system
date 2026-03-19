package com.auction.onlineauction.service;

import com.auction.onlineauction.entity.Auction;
import com.auction.onlineauction.entity.Bid;
import com.auction.onlineauction.repository.AuctionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class AuctionScheduler {

    private static final Logger log = LoggerFactory.getLogger(AuctionScheduler.class);

    private final AuctionRepository auctionRepository;
    private final BidService bidService;

    public AuctionScheduler(AuctionRepository auctionRepository, BidService bidService) {
        this.auctionRepository = auctionRepository;
        this.bidService = bidService;
    }

    @Scheduled(fixedDelayString = "PT1M")
    public void closeExpiredAuctions() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Checking for expired auctions at {}", now);
        
        List<Auction> expired = auctionRepository.findByClosedFalseAndEndTimeBefore(now);
        log.info("Found {} expired auctions", expired.size());
        
        if (expired.isEmpty()) {
            return;
        }

        log.info("Closing {} expired auctions", expired.size());
        for (Auction auction : expired) {
            log.info("Processing auction {} - end time: {}, closed: {}", 
                    auction.getId(), auction.getEndTime(), auction.isClosed());
            
            // Determine the winner (highest bidder)
            Bid highestBid = bidService.getHighestBid(auction.getId());
            if (highestBid != null) {
                auction.setWinner(highestBid.getBidder());
                log.info("Auction {} won by user {} with bid amount {}", 
                        auction.getId(), highestBid.getBidder().getName(), highestBid.getAmount());
            } else {
                log.info("Auction {} ended with no bids", auction.getId());
            }
            auction.setClosed(true);
        }
        auctionRepository.saveAll(expired);
        log.info("Finished processing expired auctions");
    }
}
