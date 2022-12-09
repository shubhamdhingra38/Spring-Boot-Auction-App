package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Category findByName(String name);

    List<Category> findAll();
}
