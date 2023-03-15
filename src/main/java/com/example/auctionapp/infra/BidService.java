package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Auction;
import com.example.auctionapp.domain.Bid;
import com.example.auctionapp.dtos.BidDTO;
import com.example.auctionapp.domain.User;
import com.example.auctionapp.dtos.MyBidDTO;
import com.example.auctionapp.exceptions.AuctionIsClosedException;
import com.example.auctionapp.exceptions.AuctionNotFoundException;
import com.example.auctionapp.exceptions.BidAmountLessException;
import com.example.auctionapp.exceptions.BidForSelfAuctionException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BidService {

    private BidRepository bidRepository;
    private AuctionRepository auctionRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public BidService(final BidRepository bidRepository,
                      final AuctionRepository auctionRepository,
                      final UserRepository userRepository,
                      final ModelMapper modelMapper) {
        this.bidRepository = bidRepository;
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<BidDTO> findBidsForAuction(final long auctionId) {
        List<Bid> bids = bidRepository.findAllByAuctionIdOrderByAmountDesc(auctionId);
        return bids.stream().map(bid -> convertToDto(bid)).toList();
    }

    public List<MyBidDTO> findBidsPlacedByUser(final String userName) {
        final List<Bid> bids = bidRepository.findBidByPlacedByUsernameOrderByPlacedAtDesc(userName);
        return bids.stream().map(bid -> modelMapper.map(bid, MyBidDTO.class)).collect(Collectors.toList());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void placeBidForAuction(final long auctionId,
                                   final String userName,
                                   final double amount,
                                   final String comment) throws AuctionNotFoundException, BidAmountLessException, BidForSelfAuctionException, AuctionIsClosedException {
        final Optional<Auction> auction = auctionRepository.findById(auctionId);
        final User user = userRepository.findByUsername(userName);

        final Bid bid = new Bid();
        bid.setAuction(auction.orElseThrow(() -> new AuctionNotFoundException()));
        bid.setPlacedBy(user);
        bid.setAmount(amount);
        bid.setComment(comment);


        if (auction.get().getIsClosed()) {
            throw new AuctionIsClosedException();
        }
        if (auction.get().getCurrentHighestBid() != null && auction.get().getCurrentHighestBid().getAmount() >= amount) {
            throw new BidAmountLessException();
        }
        if (auction.get().getCreatedBy().getId().equals(user.getId())) {
            throw new BidForSelfAuctionException();
        }
        auction.get().setCurrentHighestBid(bid);

        bidRepository.save(bid);
    }

    private BidDTO convertToDto(final Bid bid) {
        return modelMapper.map(bid, BidDTO.class);
    }
}
