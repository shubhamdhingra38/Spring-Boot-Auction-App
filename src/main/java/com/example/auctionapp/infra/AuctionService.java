package com.example.auctionapp.infra;

import com.example.auctionapp.accessors.S3Accessor;
import com.example.auctionapp.domain.*;
import com.example.auctionapp.exceptions.AuctionDoesNotBelongToUserException;
import com.example.auctionapp.exceptions.AuctionNotFoundException;
import com.example.auctionapp.exceptions.CategoryNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {
    private static int PAGE_SIZE = 4;

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

    public PaginatedAuctionsDTO findAllAuctions(int pageNumber, String createdAtOrder) {
        PageRequest pageRequest;
        if (createdAtOrder.equals("desc")) {
            pageRequest = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("createdAt").descending());
        } else {
            pageRequest = PageRequest.of(pageNumber, PAGE_SIZE);
        }
        Page<Auction> auctions = auctionRepository.findAll(pageRequest);

        List<AuctionDTO> auctionList = auctions.stream().map(auction -> convertToDto(auction)).toList();

        PaginatedAuctionsDTO paginatedAuctionsDTO = PaginatedAuctionsDTO
                .builder().auctions(auctionList).numPages(auctions.getTotalPages()).build();
        return paginatedAuctionsDTO;
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
        item.setAuction(auction);
        item.setCategory(category.get());

        auction.setCreatedBy(user);
        auction.setItem(item);
        Auction savedAuction = auctionRepository.save(auction);

        // Save image
        s3Accessor.putS3Object(image, user.getUsername());

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
