package com.example.auctionapp.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class AuctionDTO {
    private long id;
    private String name;
    private String description;
    private ItemDTO item;
    private LocalDateTime closingTime;
    private long createdById;
    private String s3ImageURL;
    private BidDTO currentHighestBid;
}
