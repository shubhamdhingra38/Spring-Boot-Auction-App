package com.example.auctionapp.restinterface;

import com.example.auctionapp.domain.*;
import com.example.auctionapp.exceptions.AuctionNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
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

    @GetMapping("/auction/{auctionId}")
    AuctionDTO getAuctionById(@PathVariable long auctionId) {
        try {
            return auctionService.findAuctionById(auctionId);
        } catch (AuctionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction does not exist");
        }
    }

    @PostMapping(value = "/auctions", consumes = {"multipart/form-data"})
    AuctionDTO createAnAuction(@RequestPart("auction") AuctionDTO auctionDTO,
                               @RequestPart(required = false) MultipartFile image,
                               Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        try {
            return auctionService.save(auctionDTO, user, image);
        } catch (CategoryNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist");
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
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
