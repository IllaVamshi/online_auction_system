package com.auction.onlineauction.service;

import com.auction.onlineauction.dto.AuctionDto;
import com.auction.onlineauction.entity.Auction;

import java.util.List;

public interface AuctionService {
    Auction createAuction(AuctionDto auctionDto, Long userId);
    Auction updateAuction(Long auctionId, AuctionDto auctionDto, Long userId);
    void deleteAuction(Long auctionId, Long userId);
    Auction getAuction(Long auctionId);
    List<Auction> getAllAuctions();
    List<Auction> getAllAuctionsIncludingClosed();
    AuctionDto toDto(Auction auction);
    void deleteAllAuctions();
}
