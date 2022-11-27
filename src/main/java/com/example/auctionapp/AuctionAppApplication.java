package com.example.auctionapp;

import com.example.auctionapp.domain.Category;
import com.example.auctionapp.domain.Item;
import com.example.auctionapp.infra.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AuctionAppApplication {
    private static final Logger log = LoggerFactory.getLogger(AuctionAppApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AuctionAppApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner demo(ItemRepository repository) {
//        return (args) -> {
//            Category c = new Category("Electronics");
//
//            repository.save(new Item("Laptop", c));
////            repository.save(new Item("Mobile", c));
//
//            log.info("Saved to repository");
//            log.info(c.toString());
//        };
//    }

    @Bean
    public CommandLineRunner demoQuery(ItemRepository repository) {
        return (args) -> {
            Item e = repository.findById(23);
            log.info("Queried...");
            log.info(e.toString());
        };
    }
}
