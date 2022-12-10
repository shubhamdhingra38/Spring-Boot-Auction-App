package com.example.auctionapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@Data
public class PaginatedAuctionsDTO {
    List<AuctionDTO> auctions;
    int numPages;
}
