package com.auction.onlineauction.service;

import com.auction.onlineauction.dto.BidDto;
import com.auction.onlineauction.entity.Bid;

import java.math.BigDecimal;
import java.util.List;

public interface BidService {
    Bid placeBid(Long bidderId, Long auctionId, BigDecimal amount);
    List<Bid> getBidsForAuction(Long auctionId);
    Bid getHighestBid(Long auctionId);
    BidDto toDto(Bid bid);
    void deleteAllBids();
}
