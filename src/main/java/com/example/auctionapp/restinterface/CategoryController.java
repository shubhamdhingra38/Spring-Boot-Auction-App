package com.example.auctionapp.restinterface;


import com.example.auctionapp.domain.Category;
import com.example.auctionapp.infra.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/categories")
    List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }
}
