package com.example.auctionapp;

import com.example.auctionapp.domain.Auction;
import com.example.auctionapp.domain.Category;
import com.example.auctionapp.domain.Item;
import com.example.auctionapp.domain.User;
import com.example.auctionapp.infra.AuctionRepository;
import com.example.auctionapp.infra.ItemRepository;
import com.example.auctionapp.infra.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootApplication
@EnableWebSecurity
public class AuctionAppApplication {
    private static final Logger log = LoggerFactory.getLogger(AuctionAppApplication.class);
    private final UserRepository userRepository;

    public AuctionAppApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        http.cors();
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/api/v1/*").allowedOrigins("http://localhost:3000");
//            }
//        };
//    }

//    @Bean
//    public CommandLineRunner createAdmin(UserRepository userRepository) {
//        return (args) -> {
//            User user = new User("admin", "password");
//            userRepository.save(user);
//        };
//    }

//    @Bean
//    public CommandLineRunner demoQuery(ItemRepository repository) {
//        return (args) -> {
//            Item e = repository.findById(23);
//            log.info("Queried...");
//            log.info(e.toString());
//        };
//    }

//    @Bean
//    public CommandLineRunner demoQuery(AuctionRepository auctionRepository,
//                                       ItemRepository itemRepository,
//                                       UserRepository userRepository) {
//        return (args) -> {
//            Category category = new Category("A category1");
//            User user = userRepository.findByUsername("test");
//
//            Auction auction = new Auction();
//            auction.setName("An auction name");
//            auction.setDescription("Auction desc");
//            auction.setCreatedBy(user);
//
//
//            Item item = new Item("An item", category);
//            item.setDescription("Item desc");
//            item.setAuction(auction);
//
//            auction.setItem(item);
//
//            auctionRepository.save(auction);
//        };
//    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
