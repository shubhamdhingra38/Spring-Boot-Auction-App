package com.example.auctionapp.restinterface;

import com.example.auctionapp.config.S3Config;
import com.example.auctionapp.domain.BidDTO;
import com.example.auctionapp.exceptions.AuctionNotFoundException;
import com.example.auctionapp.infra.BidService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class BidController {

    @Autowired
    private BidService bidService;

    @GetMapping("/auction/{auctionId}/bids")
    public List<BidDTO> findBidsForAuction(@PathVariable long auctionId) {
        return bidService.findBidsForAuction(auctionId);
    }

    @PostMapping("/auction/{auctionId}/bid")
    public void bidOnAuction(@PathVariable long auctionId, final Principal principal,
                             @RequestBody BidRequest bidRequest) {
        try {
            bidService.placeBidForAuction(auctionId, principal.getName(), bidRequest.getAmount(), bidRequest.getComment());
        } catch (AuctionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction does not exist");
        }
    }

    @Getter
    @Setter
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class BidRequest {
        private double amount;
        private String comment;
    }
}
