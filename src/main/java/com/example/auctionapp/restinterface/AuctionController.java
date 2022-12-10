package com.example.auctionapp.restinterface;

import com.example.auctionapp.domain.*;
import com.example.auctionapp.exceptions.CategoryNotFoundException;
import com.example.auctionapp.infra.AuctionRepository;
import com.example.auctionapp.infra.AuctionService;
import com.example.auctionapp.infra.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1")
@RestController
public class AuctionController {

    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/auctions")
    PaginatedAuctionsDTO getAllAuctions(@RequestParam(required = false, defaultValue = "asc") String createdAtOrder,
                                        @RequestParam(defaultValue = "0") int pageNumber) {
        return auctionService.findAllAuctions(pageNumber, createdAtOrder);
    }

    @PostMapping("/auctions")
    AuctionDTO createAnAuction(@RequestBody AuctionDTO auctionDTO, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        try {
            return auctionService.save(auctionDTO, user);
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist");
        }
    }

    private Auction convertToEntity(final AuctionDTO auctionDTO) {
        final Auction auction = modelMapper.map(auctionDTO, Auction.class);
        return auction;
    }

    private AuctionDTO convertToDto(final Auction auction) {
        final AuctionDTO auctionDTO = modelMapper.map(auction, AuctionDTO.class);
        return auctionDTO;
    }
}
