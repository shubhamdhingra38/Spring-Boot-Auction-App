package com.example.auctionapp.restinterface;

import com.example.auctionapp.dtos.BidDTO;
import com.example.auctionapp.dtos.BidRequestDTO;
import com.example.auctionapp.dtos.MyBidDTO;
import com.example.auctionapp.exceptions.AuctionIsClosedException;
import com.example.auctionapp.exceptions.AuctionNotFoundException;
import com.example.auctionapp.exceptions.BidAmountLessException;
import com.example.auctionapp.exceptions.BidForSelfAuctionException;
import com.example.auctionapp.infra.BidService;
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

    @GetMapping("/myBids")
    public List<MyBidDTO> findBidsPlacedByUser(final Principal principal) {
        final String userName = principal.getName();
        return bidService.findBidsPlacedByUser(userName);
    }

    @PostMapping("/auction/{auctionId}/bid")
    public void bidOnAuction(@PathVariable long auctionId, final Principal principal,
                             @RequestBody BidRequestDTO bidRequest) throws AuctionIsClosedException {
        try {
            bidService.placeBidForAuction(auctionId, principal.getName(), bidRequest.getAmount(), bidRequest.getComment());
        } catch (AuctionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction does not exist");
        } catch (BidAmountLessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bid amount smaller than highest bid");
        } catch (BidForSelfAuctionException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot place bid on own auction");
        }
    }


}
