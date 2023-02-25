package com.example.auctionapp.dtos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class AuctionDTO {
    private Long id;
    private String name;
    private String description;
    private ItemDTO item;
    private LocalDateTime closingTime;
    private Long createdById;
    private String s3ImageURL;
    private BidDTO currentHighestBid;
    private boolean isClosed;
}
