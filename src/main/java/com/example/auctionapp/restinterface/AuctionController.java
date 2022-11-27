package com.example.auctionapp.restinterface;

import com.example.auctionapp.domain.Auction;
import com.example.auctionapp.domain.Views;
import com.example.auctionapp.infra.AuctionRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class AuctionController {

    @Autowired
    AuctionRepository auctionRepository;

    @JsonView(Views.Public.class)
    @GetMapping("/auctions")
    List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }
}
