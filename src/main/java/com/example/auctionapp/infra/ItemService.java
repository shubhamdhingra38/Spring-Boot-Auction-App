package com.example.auctionapp.infra;

import com.example.auctionapp.domain.*;
import com.example.auctionapp.exceptions.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ItemService {


    @Autowired
    ItemRepository itemRepository;

    @Autowired
    AuctionRepository auctionRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public Item saveNewItem(Item item, User user, long auctionId, long categoryId) throws CategoryNotFoundException {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        Optional<Category> category = categoryRepository.findById(categoryId);

        if (!category.isPresent()) {
            throw new CategoryNotFoundException();
        }


        if (!auction.isPresent()) {
            throw new RuntimeException("Bad!");
        }
        if (auction.get().getCreatedBy().getId() !=  user.getId()) {
            throw new RuntimeException("Bad!!");
        }
        item.setAuction(auction.get());
        item.setCategory(category.get());
        itemRepository.save(item);
        return item;
    }
}
