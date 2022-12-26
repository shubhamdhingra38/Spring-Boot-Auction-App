package com.example.auctionapp.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class BidDTO {
    long id;
    LocalDateTime placedAt;
    double amount;
    String placedByUsername;
    String comment;
}
