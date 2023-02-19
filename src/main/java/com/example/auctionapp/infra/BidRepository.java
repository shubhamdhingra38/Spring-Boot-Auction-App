package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findAllByAuctionIdOrderByAmountDesc(long auctionId);
    List<Bid> findBidByPlacedByUsernameOrderByPlacedAtDesc(final String userName);
}
