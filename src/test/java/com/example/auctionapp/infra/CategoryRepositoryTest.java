package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Category;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
// Refer: https://stackoverflow.com/questions/34617152/how-to-re-create-database-before-each-test-in-spring
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private static final String ELECTRONICS_CATEGORY = "ELECTRONICS";
    private static final String FURNITURE_CATEGORY   = "FURNITURE";

    @BeforeAll
    public static void setup(@Autowired CategoryRepository categoryRepository) {
        final Category electronicsCategory = new Category(ELECTRONICS_CATEGORY);
        final Category furnitureCategory = new Category(FURNITURE_CATEGORY);
        categoryRepository.save(electronicsCategory);
        categoryRepository.save(furnitureCategory);
    }


    @Test
    public void testCategoryRepository_findByName_whenCategoryFound() {
        final Optional<Category> category = categoryRepository.findByName(ELECTRONICS_CATEGORY);
        Assertions.assertTrue(category.isPresent());
        Assertions.assertEquals(category.get().getName(), ELECTRONICS_CATEGORY);
    }

    @Test
    public void testCategoryRepository_findByName_whenCategoryNotFound() {
        final Optional<Category> category = categoryRepository.findByName("CategoryWhichDoesNotExist");
        Assertions.assertTrue(category.isEmpty());
    }

    @Test
    public void testCategoryRepository_findAll() {
        final List<Category> categoryList = categoryRepository.findAll();
        Assertions.assertEquals(categoryList.size(), 2);
    }
}
