package com.example.auctionapp.restinterface;

import com.example.auctionapp.domain.BidDTO;
import com.example.auctionapp.infra.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class BidController {

    @Autowired
    private BidService bidService;

    @GetMapping("/bids/{auctionId}")
    public List<BidDTO> findBidsForAuction(@PathVariable long auctionId) {
        return bidService.findBidsForAuction(auctionId);
    }
}
