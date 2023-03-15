package com.example.auctionapp.dtos;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
