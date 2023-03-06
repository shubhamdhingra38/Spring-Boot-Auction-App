package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findByName(String name);


    List<Category> findAll();
}
