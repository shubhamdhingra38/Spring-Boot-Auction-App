package com.example.auctionapp.infra;

import com.example.auctionapp.domain.*;
import com.example.auctionapp.dtos.ItemDTO;
import com.example.auctionapp.exceptions.CategoryNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ItemService {


    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Item saveNewItem(ItemDTO itemDto) throws CategoryNotFoundException {
        Item item = convertToEntity(itemDto);
        item.setAuction(null);

        Optional<Category> category = categoryRepository.findById(itemDto.getCategory().getId());

        if (!category.isPresent()) {
            throw new CategoryNotFoundException();
        }

        item.setCategory(category.get());
        itemRepository.save(item);
        return item;
    }

    private Item convertToEntity(ItemDTO itemDTO) {
        Item item = modelMapper.map(itemDTO, Item.class);
        item.setId(null);
        return item;
    }

    private ItemDTO convertToDto(Item item) {
        ItemDTO itemDTO = modelMapper.map(item, ItemDTO.class);
        return itemDTO;
    }
}
