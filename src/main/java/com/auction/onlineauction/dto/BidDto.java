package com.auction.onlineauction.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BidDto {
    private Long id;
    private Long auctionId;
    private String auctionTitle;
    private Long bidderId;
    private String bidderName;
    private BigDecimal amount;
    private LocalDateTime bidTime;
}
