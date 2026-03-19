package com.auction.onlineauction.repository;

import com.auction.onlineauction.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByEndTimeAfter(LocalDateTime now);
    List<Auction> findByClosedFalseAndEndTimeBefore(LocalDateTime now);
    List<Auction> findByClosedFalseAndEndTimeAfter(LocalDateTime now);
    
    @Query("SELECT a FROM Auction a LEFT JOIN FETCH a.winner WHERE a.id = :id")
    Optional<Auction> findByIdWithWinner(@Param("id") Long id);
    
    @Query("SELECT a FROM Auction a LEFT JOIN FETCH a.winner")
    List<Auction> findAllWithWinners();
    
    @Query("SELECT a FROM Auction a LEFT JOIN FETCH a.winner WHERE a.closed = false AND a.endTime > :now")
    List<Auction> findOpenAuctionsWithWinners(@Param("now") LocalDateTime now);
}
