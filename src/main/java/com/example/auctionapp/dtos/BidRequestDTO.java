package com.example.auctionapp.dtos;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidRequestDTO {
    private double amount;
    private String comment;
}
