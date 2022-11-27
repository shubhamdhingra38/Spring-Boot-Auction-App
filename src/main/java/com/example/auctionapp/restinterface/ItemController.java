package com.example.auctionapp.restinterface;


import com.example.auctionapp.domain.Item;
import com.example.auctionapp.domain.User;
import com.example.auctionapp.infra.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Keeping as secured controller
 */
@RequestMapping("/api/v1")
@RestController
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/items")
    List<Item> helloWorld() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(auth.getPrincipal());
//        User user = (User)auth.getPrincipal();
//        System.out.println(user);
        return itemRepository.findByName("Laptop");
    }
}
