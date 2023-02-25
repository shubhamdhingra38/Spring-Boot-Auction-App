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

    @Query(value = "SELECT auction.* FROM AUCTION auction\n" +
            "LEFT JOIN BID bid\n" +
            "ON bid.auction_id = auction.id\n" +
            "GROUP BY auction.id\n" +
            "ORDER BY\n" +
            "CASE WHEN ?1 = 'asc' THEN COUNT(bid.id) END ASC,\n" +
            "CASE WHEN ?1 = 'desc' THEN COUNT(bid.id) END DESC",
            countQuery = "SELECT COUNT(*) FROM AUCTION auction\n" +
                    "LEFT  JOIN BID bid\n" +
                    "ON bid.auction_id = auction.id\n",
            nativeQuery = true)
    Page<Auction> findAllByBidsFrequency(final String bidsFrequencyOrder, Pageable pageable);

    @Query("SELECT auction FROM Auction auction ORDER BY auction.closingTime DESC")
    List<Auction> findAllByClosingTimeDesc();

    List<Auction> findAuctionByCreatedByUsernameOrderByCreatedAtDesc(final String userName);
}
