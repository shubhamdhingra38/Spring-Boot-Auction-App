package com.example.auctionapp.restinterface;

import com.example.auctionapp.domain.*;
import com.example.auctionapp.dtos.AuctionDTO;
import com.example.auctionapp.dtos.PaginatedAuctionsDTO;
import com.example.auctionapp.exceptions.AuctionNotFoundException;
import com.example.auctionapp.exceptions.CategoryNotFoundException;
import com.example.auctionapp.infra.AuctionService;
import com.example.auctionapp.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class AuctionController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuctionService auctionService;


    @GetMapping("/auctions")
    PaginatedAuctionsDTO getAllAuctions(@RequestParam(required = false, defaultValue = "createdAt") String sortBy,
                                        @RequestParam(required = false, defaultValue = "asc") String sortOrder,
                                        @RequestParam(required = false, defaultValue = "") String category,
                                        @RequestParam(defaultValue = "0") int pageNumber) {
        return auctionService.findAllAuctions(pageNumber, sortBy, sortOrder, category);
    }

    @GetMapping("/auctions/search")
    List<AuctionDTO> searchAuctions(@RequestParam("query") String searchQuery) {
        return auctionService.findAuctionsByTextSearch(searchQuery);
    }

    @GetMapping("/myAuctions")
    List<AuctionDTO> getMyAuctions(final Principal principal) {
        final String userName = principal.getName();
        return auctionService.findAllAuctionsByUser(userName);
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
}
