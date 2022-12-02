package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Auction;
import org.springframework.beans.factory.annotation.Autowired;

public class AuctionService {

    @Autowired
    AuctionRepository auctionRepository;

}
