package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Auction;
import com.example.auctionapp.domain.Bid;
import com.example.auctionapp.dtos.BidDTO;
import com.example.auctionapp.domain.User;
import com.example.auctionapp.exceptions.AuctionNotFoundException;
import com.example.auctionapp.exceptions.BidAmountLessException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private UserRepository userRepository;

    public List<BidDTO> findBidsForAuction(final long auctionId) {
        List<Bid> bids = bidRepository.findAllByAuctionIdOrderByAmountDesc(auctionId);
        return bids.stream().map(bid -> convertToDto(bid)).toList();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void placeBidForAuction(final long auctionId,
                                   final String userName,
                                   final double amount,
                                   final String comment) throws AuctionNotFoundException, BidAmountLessException {
        final Optional<Auction> auction = auctionRepository.findById(auctionId);
        final User user = userRepository.findByUsername(userName);

        final Bid bid = new Bid();
        bid.setAuction(auction.orElseThrow(() -> new AuctionNotFoundException()));
        bid.setPlacedBy(user);
        bid.setAmount(amount);
        bid.setComment(comment);
        // TODO: Check if bid is greater than previous bids and concurrency control

        if (auction.get().getCurrentHighestBid() != null && auction.get().getCurrentHighestBid().getAmount() >= amount) {
            throw new BidAmountLessException();
        }
        auction.get().setCurrentHighestBid(bid);

        bidRepository.save(bid);
    }

    private BidDTO convertToDto(final Bid bid) {
        return modelMapper.map(bid, BidDTO.class);
    }
}
