package com.example.auctionapp.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
