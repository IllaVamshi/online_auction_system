package com.auction.onlineauction.service;

import com.auction.onlineauction.dto.AuctionDto;
import com.auction.onlineauction.entity.Auction;
import com.auction.onlineauction.entity.User;
import com.auction.onlineauction.repository.AuctionRepository;
import com.auction.onlineauction.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionServiceImpl implements AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    public AuctionServiceImpl(AuctionRepository auctionRepository, UserRepository userRepository) {
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Auction createAuction(AuctionDto auctionDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Auction auction = Auction.builder()
                .title(auctionDto.getTitle())
                .description(auctionDto.getDescription())
                .startingPrice(auctionDto.getStartingPrice())
                .currentPrice(auctionDto.getStartingPrice())
                .endTime(auctionDto.getEndTime())
                .imageUrl(auctionDto.getImageUrl())
                .createdBy(user)
                .closed(false)
                .build();

        return auctionRepository.save(auction);
    }

    @Override
    @Transactional
    public Auction updateAuction(Long auctionId, AuctionDto auctionDto, Long userId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));

        if (!auction.getCreatedBy().getId().equals(userId)) {
            throw new IllegalArgumentException("Only the creator can update this auction");
        }
        if (auction.isClosed()) {
            throw new IllegalArgumentException("Cannot update a closed auction");
        }

        auction.setTitle(auctionDto.getTitle());
        auction.setDescription(auctionDto.getDescription());
        auction.setStartingPrice(auctionDto.getStartingPrice());
        auction.setEndTime(auctionDto.getEndTime());
        auction.setImageUrl(auctionDto.getImageUrl());
        return auctionRepository.save(auction);
    }

    @Override
    @Transactional
    public void deleteAuction(Long auctionId, Long userId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));
        if (!auction.getCreatedBy().getId().equals(userId)) {
            throw new IllegalArgumentException("Only the creator can delete this auction");
        }
        auctionRepository.delete(auction);
    }

    @Override
    public Auction getAuction(Long auctionId) {
        return auctionRepository.findByIdWithWinner(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Auction not found"));
    }

    @Override
    public List<Auction> getAllAuctions() {
        // Only active (open) auctions with winner info – used by dashboard
        return auctionRepository.findOpenAuctionsWithWinners(LocalDateTime.now());
    }

    @Override
    public List<Auction> getAllAuctionsIncludingClosed() {
        return auctionRepository.findAllWithWinners();
    }

    @Override
    public AuctionDto toDto(Auction auction) {
        AuctionDto dto = new AuctionDto();
        dto.setId(auction.getId());
        dto.setTitle(auction.getTitle());
        dto.setDescription(auction.getDescription());
        dto.setStartingPrice(auction.getStartingPrice());
        dto.setCurrentPrice(auction.getCurrentPrice());
        dto.setEndTime(auction.getEndTime());
        dto.setImageUrl(auction.getImageUrl());
        dto.setCreatedById(auction.getCreatedBy() != null ? auction.getCreatedBy().getId() : null);
        dto.setCreatedByName(auction.getCreatedBy() != null ? auction.getCreatedBy().getName() : null);
        dto.setClosed(auction.isClosed());
        dto.setWinnerId(auction.getWinner() != null ? auction.getWinner().getId() : null);
        dto.setWinnerName(auction.getWinner() != null ? auction.getWinner().getName() : null);
        return dto;
    }

    @Override
    @Transactional
    public void deleteAllAuctions() {
        auctionRepository.deleteAll();
    }
}
