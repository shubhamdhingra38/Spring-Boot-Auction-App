package com.example.auctionapp.infra;

import com.example.auctionapp.domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
// Refer: https://stackoverflow.com/questions/34617152/how-to-re-create-database-before-each-test-in-spring
class AuctionRepositoryTest {
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public static void setup(@Autowired AuctionRepository auctionRepository,
                             @Autowired UserRepository userRepository,
                             @Autowired ProfileRepository profileRepository) {


        final User user1 = TestUtils.createUser(userRepository);
        final User user2 = TestUtils.createUser(userRepository);

        final Category category1 = new Category("Category1");
        final Auction auction1 = new Auction("Auction1", "Description1");
        final Item item1 = new Item("Item1", category1);
        auction1.setCreatedBy(user1);
        auction1.setItem(item1);
        item1.setAuction(auction1);
        auctionRepository.save(auction1);

        final Category category2 = new Category("Category2");
        final Auction auction2 = new Auction("Auction2", "Description2");
        final Item item2 = new Item("Item2", category2);
        auction2.setCreatedBy(user1);
        auction2.setItem(item2);
        item2.setAuction(auction2);

        final Bid bid = new Bid();
        bid.setAuction(auction2);
        bid.setPlacedBy(user2);
        bid.setAmount(101);
        auction2.setCurrentHighestBid(bid);
        auctionRepository.save(auction2);
    }

    @Test
    public void testAuctionRepository_findAll() {
        final Pageable pageable = PageRequest.of(0, 2);
        final Page<Auction> auctionsPage = auctionRepository.findAll(pageable);
        Assertions.assertEquals(auctionsPage.getSize(), 2);
    }

    @Test
    public void testAuctionRepository_findAll_worksWithPagination() {
        final Pageable pageable = PageRequest.of(0, 1);
        final Page<Auction> auctionsPage = auctionRepository.findAll(pageable);
        Assertions.assertEquals(auctionsPage.getSize(), 1);
        Assertions.assertEquals(auctionsPage.getTotalPages(), 2);
    }

    @Test
    public void testAuctionRepository_findAllByItemCategoryId() {
        final Category category = categoryRepository.findByName("Category1").get();
        final Page<Auction> auctionsPage = auctionRepository.findAllByItemCategoryId(category.getId(), PageRequest.of(0, 2));
        Assertions.assertEquals(auctionsPage.getTotalElements(), 1);
        Assertions.assertEquals(auctionsPage.getTotalPages(), 1);
    }

    @Test
    public void testAuctionRepository_findAllByBidsFrequency() {
        final Page<Auction> auctionsPageAsc = auctionRepository.findAllByBidsFrequency("asc", PageRequest.of(0, 2));
        Assertions.assertEquals(auctionsPageAsc.getTotalElements(), 2);
        Assertions.assertNull(auctionsPageAsc.stream().toList().get(0).getCurrentHighestBid());

        final Page<Auction> auctionsPageDesc = auctionRepository.findAllByBidsFrequency("desc", PageRequest.of(0, 2));
        Assertions.assertEquals(auctionsPageDesc.getTotalElements(), 2);
        Assertions.assertNotNull(auctionsPageDesc.stream().toList().get(0).getCurrentHighestBid());
    }

    @Test
    public void testAuctionRepository_findAllByBidsFrequencyAndCategory() {
        final Category category = categoryRepository.findByName("Category1").get();
        final Page<Auction> auctionsPageAsc = auctionRepository.findAllByBidsFrequencyAndCategory("asc", category.getId(), PageRequest.of(0, 2));
        Assertions.assertEquals(auctionsPageAsc.getTotalElements(), 1);
    }

    @Test
    public void testAuctionRepository_findAllByClosingTimeDesc() {
        final List<Auction> auctionsList = auctionRepository.findAllByClosingTimeDesc();
        Assertions.assertEquals(auctionsList.size(), 2);
        Assertions.assertEquals(auctionsList.get(0).getName(), "Auction2");
    }

    @Test
    public void testAuctionRepository_findAuctionByCreatedByUsernameOrderByCreatedAtDesc() {
        final User user = userRepository.findAll().get(0);
        final List<Auction> auctionList = auctionRepository.findAuctionByCreatedByUsernameOrderByCreatedAtDesc(user.getUsername());
        Assertions.assertEquals(auctionList.size(), 2);
    }
}


