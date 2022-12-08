package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Auction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionService {

    @Autowired
    AuctionRepository auctionRepository;


    public List<Auction> findAllAuctions() {
        return auctionRepository.findAll();
    }


}
