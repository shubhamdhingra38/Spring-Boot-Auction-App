package com.example.auctionapp;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
@EnableWebSecurity
public class AuctionAppApplication {
    private static final Logger log = LoggerFactory.getLogger(AuctionAppApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AuctionAppApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner createAuction(AuctionRepository auctionRepository, UserRepository userRepository) {
//        return (args) -> {
//            User user = userRepository.findByUsername("test");
//            Auction auction = new Auction("Some furniture", "Vintage furniture for sale");
//            auction.setCreatedBy(user);
//            auctionRepository.save(auction);
//        };
//    }

//    @Bean
//    public CommandLineRunner createItem(ItemRepository repository) {
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

    /**
     * This works but weird syntax though?
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().httpBasic().and()
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/", "/home").permitAll()
                        .anyRequest().authenticated()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }


//    @Bean
//    public CommandLineRunner demoQuery(ItemRepository repository) {
//        return (args) -> {
//            Item e = repository.findById(23);
//            log.info("Queried...");
//            log.info(e.toString());
//        };
//    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
