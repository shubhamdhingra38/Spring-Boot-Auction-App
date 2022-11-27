package com.example.auctionapp.infra;


import com.example.auctionapp.domain.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findByName(String name);

    Item findById(long id);
}
