package com.auction.onlineauction.repository;

import com.auction.onlineauction.entity.Auction;
import com.auction.onlineauction.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuctionOrderByAmountDesc(Auction auction);
    Optional<Bid> findFirstByAuctionOrderByAmountDesc(Auction auction);

    @Query("SELECT b FROM Bid b LEFT JOIN FETCH b.bidder WHERE b.auction = :auction ORDER BY b.amount DESC")
    List<Bid> findByAuctionWithBidderOrderByAmountDesc(Auction auction);

    @Query("SELECT b FROM Bid b LEFT JOIN FETCH b.bidder WHERE b.auction = :auction ORDER BY b.amount DESC LIMIT 1")
    Optional<Bid> findFirstByAuctionWithBidderOrderByAmountDesc(Auction auction);
}
