package com.example.auctionapp.restinterface;


import com.example.auctionapp.domain.Item;
import com.example.auctionapp.dtos.ItemDTO;
import com.example.auctionapp.infra.ItemRepository;
import com.example.auctionapp.infra.ItemService;
import com.example.auctionapp.infra.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Keeping as secured controller
 */
@RequestMapping("/api/v1")
@RestController
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/items")
    List<ItemDTO> listItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream().map(item -> convertToDto(item)).toList();
    }

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("/items")
//    ItemDTO createAnItem(@RequestBody ItemDTO itemDTO, Principal principal) {
//        User user = userRepository.findByUsername(principal.getName());
//        try {
//            final Item responseItem = itemService.saveNewItem(itemDTO, user);
//            return convertToDto(responseItem);
//        } catch (CategoryNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category does not exist");
//        } catch (AuctionDoesNotBelongToUserException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Auction does not belong to user");
//        } catch (AuctionNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Auction does not exist");
//        }
//    }

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
