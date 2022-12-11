package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Bid;
import com.example.auctionapp.domain.BidDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<BidDTO> findBidsForAuction(final long auctionId) {
        List<Bid> bids = bidRepository.findAllByAuctionId(auctionId);
        return bids.stream().map(bid -> convertToDto(bid)).toList();
    }

    private BidDTO convertToDto(final Bid bid) {
        return modelMapper.map(bid, BidDTO.class);
    }
}
