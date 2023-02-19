package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AuctionRepository extends PagingAndSortingRepository<Auction, Long> {

    @Override
    Page<Auction> findAll(Pageable pageable);

    @Query("SELECT auction FROM Auction auction ORDER BY auction.closingTime DESC")
    List<Auction> findAllByClosingTimeDesc();

    List<Auction> findAuctionByCreatedByUsernameOrderByCreatedAtDesc(final String userName);
}
