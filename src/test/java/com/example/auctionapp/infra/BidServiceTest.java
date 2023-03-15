package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Auction;
import com.example.auctionapp.domain.Bid;
import com.example.auctionapp.domain.User;
import com.example.auctionapp.dtos.BidDTO;
import com.example.auctionapp.dtos.MyBidDTO;
import com.example.auctionapp.exceptions.AuctionIsClosedException;
import com.example.auctionapp.exceptions.AuctionNotFoundException;
import com.example.auctionapp.exceptions.BidAmountLessException;
import com.example.auctionapp.exceptions.BidForSelfAuctionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BidServiceTest {

    final ModelMapper modelMapper = new ModelMapper();

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    private BidService bidService;

    @BeforeEach
    public void setUp() {
        bidService = new BidService(bidRepository, auctionRepository, userRepository, modelMapper);
    }

    @Test
    public void testFindBidsForAuction() {
        final User user = new User();
        final Bid bid = new Bid();
        bid.setAmount(1.0);
        bid.setPlacedBy(user);

        Mockito.when(bidRepository.findAllByAuctionIdOrderByAmountDesc(1)).thenReturn(List.of(bid));
        final List<BidDTO> bids = bidService.findBidsForAuction(1);

        Assertions.assertEquals(bids.size(), 1);
        Assertions.assertEquals(bids.get(0).getAmount(), 1.0);
    }

    @Test
    public void testFindBidsPlacedByUser() {
        final User user = new User();
        user.setUsername("username");
        final Bid bid = new Bid();
        bid.setAmount(1.0);
        bid.setPlacedBy(user);

        Mockito.when(bidRepository.findBidByPlacedByUsernameOrderByPlacedAtDesc(user.getUsername())).thenReturn(List.of(bid));
        final List<MyBidDTO> bids = bidService.findBidsPlacedByUser("username");

        Assertions.assertEquals(bids.size(), 1);
        Assertions.assertEquals(user.getUsername(), bids.get(0).getPlacedByUsername());
    }

    @Test
    public void test_placeBidForAuction_savesBid() throws AuctionIsClosedException, BidForSelfAuctionException, AuctionNotFoundException, BidAmountLessException {
        final Auction auction = new Auction();
        final User auctionCreator = new User();
        auctionCreator.setId(1L);
        auction.setCreatedBy(auctionCreator);
        final LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        auction.setClosingTime(localDateTime.plusWeeks(2));
        final User user = new User();
        user.setId(2L);

        Mockito.when(auctionRepository.findById(Mockito.any())).thenReturn(Optional.of(auction));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(user);

        bidService.placeBidForAuction(1, "username", 2.0, "comment");
        Mockito.verify(bidRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void test_placeBidForAuction_throwsAuctionIsClosedException() {
        final Auction auction = new Auction();
        final User auctionCreator = new User();
        auctionCreator.setId(1L);
        auction.setCreatedBy(auctionCreator);
        final LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        auction.setClosingTime(localDateTime.minusWeeks(2));
        final User user = new User();
        user.setId(2L);

        Mockito.when(auctionRepository.findById(Mockito.any())).thenReturn(Optional.of(auction));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(user);

        Assertions.assertThrows(AuctionIsClosedException.class, () -> bidService.placeBidForAuction(1, "username", 2.0, "comment"));
    }

    @Test
    public void test_placeBidForAuction_throwsBidAmountLessException() {
        final Bid currentHighestBid = new Bid();
        currentHighestBid.setAmount(1000.0);

        final Auction auction = new Auction();
        final User auctionCreator = new User();
        auctionCreator.setId(1L);
        auction.setCreatedBy(auctionCreator);
        auction.setCurrentHighestBid(currentHighestBid);
        final LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        auction.setClosingTime(localDateTime.plusWeeks(2));
        final User user = new User();
        user.setId(2L);

        Mockito.when(auctionRepository.findById(Mockito.any())).thenReturn(Optional.of(auction));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(user);

        Assertions.assertThrows(BidAmountLessException.class, () -> bidService.placeBidForAuction(1, "username", 2.0, "comment"));
    }

    @Test
    public void test_placeBidForAuction_throwsBidForSelfAuctionException() {
        final Auction auction = new Auction();
        final User auctionCreator = new User();
        auctionCreator.setId(1L);
        auction.setCreatedBy(auctionCreator);
        final LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("UTC"));
        auction.setClosingTime(localDateTime.plusWeeks(2));
        final User user = new User();
        user.setId(1L);

        Mockito.when(auctionRepository.findById(Mockito.any())).thenReturn(Optional.of(auction));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(user);

        Assertions.assertThrows(BidForSelfAuctionException.class, () -> bidService.placeBidForAuction(1, "username", 2.0, "comment"));
    }


}
