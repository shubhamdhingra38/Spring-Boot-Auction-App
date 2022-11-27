package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Auction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuctionRepository extends CrudRepository<Auction, Long> {
    @Override
    List<Auction> findAll();

}
