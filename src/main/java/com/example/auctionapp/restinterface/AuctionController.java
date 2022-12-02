package com.example.auctionapp.restinterface;

import com.example.auctionapp.domain.Auction;
import com.example.auctionapp.domain.User;
import com.example.auctionapp.domain.Views;
import com.example.auctionapp.infra.AuctionRepository;
import com.example.auctionapp.infra.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class AuctionController {

    @Autowired
    AuctionRepository auctionRepository;
    @Autowired
    UserRepository userRepository;

    /**
     * TODO: Currently controller directly interacting with Data layer
     *  Add a service layer
     */
    @JsonView(Views.Public.class)
    @GetMapping("/auctions")
    List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }

    @JsonView(Views.Public.class)
    @PostMapping("/auctions")
    Auction createAnAuction(@RequestBody Auction auction, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        auction.setCreatedBy(user);
        auctionRepository.save(auction);
        return auction;
    }
}
