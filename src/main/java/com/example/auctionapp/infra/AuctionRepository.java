package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Auction;
import org.springframework.data.repository.CrudRepository;

public interface AuctionRepository extends CrudRepository<Auction, Long> {
}
