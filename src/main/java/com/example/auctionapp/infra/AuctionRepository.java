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

    Page<Auction> findAllByItemCategoryId(final Long categoryId, final Pageable pageable);

    @Query(value = "SELECT auction.* FROM auction\n" +
            "LEFT JOIN bid\n" +
            "ON bid.auction_id = auction.id\n" +
            "GROUP BY auction.id\n" +
            "ORDER BY\n" +
            "CASE WHEN ?1 = 'asc' THEN COUNT(bid.id) END ASC,\n" +
            "CASE WHEN ?1 = 'desc' THEN COUNT(bid.id) END DESC",
            countQuery = "SELECT COUNT(*) FROM auction\n" +
                    "LEFT  JOIN bid\n" +
                    "ON bid.auction_id = auction.id\n",
            nativeQuery = true)
    Page<Auction> findAllByBidsFrequency(final String bidsFrequencyOrder, final Pageable pageable);

    @Query(value = "SELECT auction.*, item.category_id FROM auction\n" +
            "LEFT JOIN bid\n" +
            "ON bid.auction_id = auction.id\n" +
            "INNER JOIN item\n" +
            "ON item.auction_id = auction.id\n" +
            "WHERE item.category_id = ?2\n" +
            "GROUP BY auction.id\n" +
            "ORDER BY\n" +
            "CASE WHEN ?1 = 'asc' THEN COUNT(bid.id) END ASC,\n" +
            "CASE WHEN ?1 = 'desc' THEN COUNT(bid.id) END DESC",
            countQuery = "SELECT COUNT(*) FROM auction\n" +
                    "LEFT JOIN bid\n" +
                    "ON bid.auction_id = auction.id\n" +
                    "INNER JOIN item\n" +
                    "ON item.auction_id = auction.id\n" +
                    "WHERE item.category_id = ?2\n",
            nativeQuery = true)
    Page<Auction> findAllByBidsFrequencyAndCategory(final String bidsFrequencyOrder, final Long categoryId, final Pageable pageable);

    @Query("SELECT auction FROM Auction auction ORDER BY auction.closingTime DESC")
    List<Auction> findAllByClosingTimeDesc();

    List<Auction> findAuctionByCreatedByUsernameOrderByCreatedAtDesc(final String userName);

    @Query(value = "SELECT *\n" +
            "FROM auction\n" +
            "INNER JOIN item\n" +
            "ON item.auction_id = auction.id\n" +
            "WHERE MATCH(auction.name, auction.description)\n" +
            "AGAINST (?1)\n" +
            "OR MATCH(item.name, item.description)\n" +
            "AGAINST (?1)", nativeQuery = true)
    List<Auction> findAuctionsByTextSearch(final String text);
}
