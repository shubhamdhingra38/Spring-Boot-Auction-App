package com.example.auctionapp.infra;

import com.example.auctionapp.accessors.S3Accessor;
import com.example.auctionapp.domain.*;
import com.example.auctionapp.dtos.AuctionDTO;
import com.example.auctionapp.dtos.ItemDTO;
import com.example.auctionapp.dtos.PaginatedAuctionsDTO;
import com.example.auctionapp.exceptions.AuctionIsClosedException;
import com.example.auctionapp.exceptions.AuctionNotFoundException;
import com.example.auctionapp.exceptions.CategoryNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

enum SortAuctionOrder {
    BIDS("bids"),
    CREATED_AT("createdAt");

    public final String label;

    private SortAuctionOrder(String label) {
        this.label = label;
    }
}

@Service
public class AuctionService {
    private final String AUCTION_DIRECTORY = "auction";
    private static final int PAGE_SIZE = 4;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private S3Accessor s3Accessor;

    public AuctionDTO findAuctionById(long auctionId) throws AuctionNotFoundException {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        return convertToDto(auction.orElseThrow(() -> new AuctionNotFoundException()));
    }

    public List<AuctionDTO> findAllAuctionsByUser(final String userName) {
        List<Auction> auctionsList = auctionRepository.findAuctionByCreatedByUsernameOrderByCreatedAtDesc(userName);
        return auctionsList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public PaginatedAuctionsDTO findAllAuctions(int pageNumber, String sortBy, String sortOrder) {
        Page<Auction> auctions;
        if (sortBy.equals(SortAuctionOrder.BIDS.label)) {
            auctions = findAllAuctionsSortByBidsOrder(sortOrder, pageNumber);
        } else if (sortBy.equals(SortAuctionOrder.CREATED_AT.label)) {
            auctions = findAllAuctionsSortByCreatedAtOrder(sortOrder, pageNumber);
        } else {
            auctions = findAllAuctions(pageNumber);
        }
        List<AuctionDTO> auctionList = auctions.stream().map(auction -> convertToDto(auction)).toList();

        PaginatedAuctionsDTO paginatedAuctionsDTO = PaginatedAuctionsDTO
                .builder().auctions(auctionList).numPages(auctions.getTotalPages()).build();
        return paginatedAuctionsDTO;
    }

    private Page<Auction> findAllAuctions(final int pageNumber) {
        Page<Auction> auctions = auctionRepository.findAll(PageRequest.of(pageNumber, PAGE_SIZE));
        return auctions;
    }

    private Page<Auction> findAllAuctionsSortByCreatedAtOrder(final String createdAtOrder, final int pageNumber) {
        PageRequest pageRequest;
        if (createdAtOrder.equals("desc")) {
            pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("createdAt").descending());
        } else {
            pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("createdAt").ascending());
        }
        Page<Auction> auctions = auctionRepository.findAll(pageRequest);
        return auctions;
    }

    private Page<Auction> findAllAuctionsSortByBidsOrder(final String bidsOrder, final int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        Page<Auction> auctions = auctionRepository.findAllByBidsFrequency(bidsOrder.toLowerCase(), pageRequest);
        return auctions;
    }

    @Transactional
    public AuctionDTO save(AuctionDTO auctionDTO, User user, MultipartFile image) throws CategoryNotFoundException, IOException {
        Auction auction = convertToEntity(auctionDTO);
        ItemDTO itemDto = auctionDTO.getItem();

        Optional<Category> category = categoryRepository.findById(itemDto.getCategory().getId());

        if (!category.isPresent()) {
            throw new CategoryNotFoundException();
        }
        Item item = modelMapper.map(itemDto, Item.class);
        item.setId(null);
        item.setAuction(auction);
        item.setCategory(category.get());

        auction.setCreatedBy(user);
        auction.setItem(item);

        // Save image
        if (image != null && !image.isEmpty()) {
            final String directoryName = user.getUsername() + File.separator + AUCTION_DIRECTORY;
            final String s3ImageUrl = s3Accessor.putS3Object(image, directoryName);
            auction.setS3ImageURL(s3ImageUrl);
        }

        Auction savedAuction = auctionRepository.save(auction);
        return convertToDto(savedAuction);
    }

    public List<Auction> findAllAuctionsByClosingTimeDesc() {
        return auctionRepository.findAllByClosingTimeDesc();
    }

    private Auction convertToEntity(final AuctionDTO auctionDTO) {
        final Auction auction = modelMapper.map(auctionDTO, Auction.class);
        return auction;
    }

    private AuctionDTO convertToDto(final Auction auction) {
        final AuctionDTO auctionDTO = modelMapper.map(auction, AuctionDTO.class);
        return auctionDTO;
    }
}
