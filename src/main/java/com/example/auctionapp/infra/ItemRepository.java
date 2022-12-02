package com.example.auctionapp.infra;


import com.example.auctionapp.domain.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/***
 * Stuff with Spring Data JPA
 * Don't have to write custom DAOs and Repositories?
 */
public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item> findByName(String name);

    @Override
    List<Item> findAll();

    Item findById(long id);
}
