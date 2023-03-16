package com.example.auctionapp.infra;

import com.example.auctionapp.accessors.S3Accessor;
import com.example.auctionapp.domain.Auction;
import com.example.auctionapp.domain.Category;
import com.example.auctionapp.domain.User;
import com.example.auctionapp.dtos.AuctionDTO;
import com.example.auctionapp.dtos.CategoryDTO;
import com.example.auctionapp.dtos.ItemDTO;
import com.example.auctionapp.exceptions.AuctionNotFoundException;
import com.example.auctionapp.exceptions.CategoryNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AuctionServiceTest {
    final ModelMapper modelMapper = new ModelMapper();

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private S3Accessor s3Accessor;

    private AuctionService auctionService;

    @BeforeEach
    public void setUp() {
        auctionService = new AuctionService(modelMapper, auctionRepository, categoryRepository, s3Accessor);
    }

    @Test
    public void test_findAuctionById_returnsAuction() throws AuctionNotFoundException {
        final Auction auction = createAuction();

        Mockito.when(auctionRepository.findById(Mockito.any())).thenReturn(Optional.of(auction));

        final AuctionDTO auctionDTO = auctionService.findAuctionById(1);
        Assertions.assertEquals(auctionDTO.getName(), "name");
        Assertions.assertEquals(auctionDTO.getDescription(), "description");
    }

    @Test
    public void test_findAuctionById_throwsAuctionNotFoundException() {
        Mockito.when(auctionRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(AuctionNotFoundException.class, () -> auctionService.findAuctionById(1));
    }

    @Test
    public void test_findAllAuctionsByUser() {
        final Auction auction = createAuction();

        Mockito.when(auctionRepository.findAuctionByCreatedByUsernameOrderByCreatedAtDesc(Mockito.any()))
                .thenReturn(List.of(auction));

        Assertions.assertEquals(1, auctionService.findAllAuctionsByUser("user").size());
    }

    @Test
    public void test_findAllAuctions_byCategoryAndSortByBids() {
        final Category category = new Category("category");
        final Auction auction = createAuction();
        final Page<Auction> auctionsPage = new PageImpl(List.of(auction));

        Mockito.when(categoryRepository.findByName(Mockito.any())).thenReturn(Optional.of(category));
        Mockito.when(auctionRepository.findAllByBidsFrequencyAndCategory(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(auctionsPage);

        Assertions.assertEquals(1, auctionService.findAllAuctions(1, SortAuctionOrder.BIDS.label, "asc", category.getName()).getNumPages());
    }

    @Test
    public void test_findAllAuctions_sortByBids() {
        final Auction auction = createAuction();
        final Page<Auction> auctionsPage = new PageImpl(List.of(auction));

        Mockito.when(categoryRepository.findByName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(auctionRepository.findAllByBidsFrequency(Mockito.any(), Mockito.any()))
                .thenReturn(auctionsPage);

        Assertions.assertEquals(1,
                auctionService.findAllAuctions(1, SortAuctionOrder.BIDS.label, "asc", "category").getNumPages());
    }

    @Test
    public void test_findAllAuctions_byCategoryAndSortByCreatedAt() {
        final Category category = new Category("category");
        final Auction auction = createAuction();
        final Page<Auction> auctionsPage = new PageImpl(List.of(auction));

        Mockito.when(categoryRepository.findByName(Mockito.any())).thenReturn(Optional.of(category));
        Mockito.when(auctionRepository.findAllByItemCategoryId(Mockito.any(), Mockito.any()))
                .thenReturn(auctionsPage);

        Assertions.assertEquals(1,
                auctionService.findAllAuctions(1, SortAuctionOrder.CREATED_AT.label, "asc", category.getName()).getNumPages());;
    }

    @Test
    public void test_findAllAuctions_sortByCreatedAt() {
        final Auction auction = createAuction();
        final Page<Auction> auctionsPage = new PageImpl(List.of(auction));

        Mockito.when(categoryRepository.findByName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(auctionRepository.findAll((Pageable) Mockito.any()))
                .thenReturn(auctionsPage);

        auctionService.findAllAuctions(1, SortAuctionOrder.CREATED_AT.label, "asc", "category");
        auctionService.findAllAuctions(1, SortAuctionOrder.CREATED_AT.label, "desc", "category");
    }


    @Test
    public void test_findAllAuctions_byCategory() {
        final Category category = new Category("category");
        final Auction auction = createAuction();
        final Page<Auction> auctionsPage = new PageImpl(List.of(auction));

        Mockito.when(categoryRepository.findByName(Mockito.any())).thenReturn(Optional.of(category));
        Mockito.when(auctionRepository.findAllByItemCategoryId(Mockito.any(), Mockito.any()))
                .thenReturn(auctionsPage);

        Assertions.assertEquals(1, auctionService.findAllAuctions(1, "", "asc", category.getName()).getAuctions().size());
    }


    @Test
    public void test_findAllAuctions() {
        final Auction auction = createAuction();
        final Page<Auction> auctionsPage = new PageImpl(List.of(auction));

        Mockito.when(categoryRepository.findByName(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(auctionRepository.findAll((Pageable) Mockito.any()))
                .thenReturn(auctionsPage);

        Assertions.assertEquals(1, auctionService.findAllAuctions(1, "", "asc", "category").getAuctions().size());
    }

    @Test
    public void test_save() throws CategoryNotFoundException, IOException {
        final Category category = new Category("category");
        final User user = new User();
        final Auction auction = createAuction();

        Mockito.when(categoryRepository.findById(Mockito.any())).thenReturn(Optional.of(category));
        Mockito.when(auctionRepository.save(Mockito.any())).thenReturn(auction);

        auctionService.save(createAuctionRequest(), user, null);
    }

    @Test
    public void test_save_throwsCategoryNotFoundException()  {
        final User user = new User();
        final Auction auction = createAuction();

        Mockito.when(categoryRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(auctionRepository.save(Mockito.any())).thenReturn(auction);

        Assertions.assertThrows(CategoryNotFoundException.class, () -> auctionService.save(createAuctionRequest(), user, null));
    }

    @Test
    public void test_save_withImage() throws IOException, CategoryNotFoundException {
        final Category category = new Category("category");
        final User user = new User();
        final Auction auction = createAuction();

        Mockito.when(categoryRepository.findById(Mockito.any())).thenReturn(Optional.of(category));
        Mockito.when(s3Accessor.putS3Object(Mockito.any(), Mockito.any())).thenReturn("imageURL");

        final ArgumentCaptor<Auction> argumentCaptor = ArgumentCaptor.forClass(Auction.class);
        Mockito.when(auctionRepository.save(Mockito.any())).thenReturn(auction);

        final MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );
        final AuctionDTO auctionDTO = auctionService.save(createAuctionRequest(), user, file);
        Mockito.verify(auctionRepository, Mockito.times(1)).save(argumentCaptor.capture());

        Assertions.assertEquals("imageURL", argumentCaptor.getValue().getS3ImageURL());
    }

    private Auction createAuction() {
        final Auction auction = new Auction();
        auction.setName("name");
        auction.setDescription("description");
        auction.setClosingTime(LocalDateTime.now());
        return auction;
    }

    private AuctionDTO createAuctionRequest() {
        final CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("category")
                .id(1)
                .build();
        final ItemDTO itemDTO = ItemDTO.builder()
                .name("item")
                .description("item")
                .startingPrice(200.0)
                .category(categoryDTO)
                .build();
        final AuctionDTO requestAuctionDTO = AuctionDTO.builder()
                .name("auction")
                .description("auction")
                .item(itemDTO)
                .build();
        return requestAuctionDTO;
    }

}
