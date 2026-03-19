package com.auction.onlineauction.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AuctionDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal currentPrice;
    private LocalDateTime endTime;
    private String imageUrl;
    private Long createdById;
    private String createdByName;
    private Boolean closed;
    private Long winnerId;
    private String winnerName;
}
