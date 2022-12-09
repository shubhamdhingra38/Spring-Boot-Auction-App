package com.example.auctionapp.infra;

import com.example.auctionapp.domain.*;
import com.example.auctionapp.exceptions.AuctionDoesNotBelongToUserException;
import com.example.auctionapp.exceptions.AuctionNotFoundException;
import com.example.auctionapp.exceptions.CategoryNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryRepository categoryRepository;


    public List<Auction> findAllAuctions() {
        return auctionRepository.findAll();
    }


    @Transactional
    public AuctionDTO save(AuctionDTO auctionDTO, User user) throws CategoryNotFoundException {
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

        return convertToDto(savedAuction);
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
