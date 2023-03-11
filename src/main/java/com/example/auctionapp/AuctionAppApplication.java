package com.example.auctionapp;

import com.example.auctionapp.domain.ChatMessage;
import com.example.auctionapp.dtos.ChatMessageDTO;
import com.example.auctionapp.infra.UserRepository;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
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
//    public CommandLineRunner debugS3Config() {
//        return (args) -> {
//            System.out.println("Bucket name is: " + s3Config.getBucketName());
//        };
//    }

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
        http.authorizeHttpRequests((requests) -> requests
                        .antMatchers("/", "/home", "/auth/login", "/auth/logout", "/auth/csrf", "/auth/register").permitAll()
                        .anyRequest().authenticated()
        );
        http.cors();
        http.logout().deleteCookies("JSESSIONID").invalidateHttpSession(true)
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "POST"));

        // Basic Auth should only be for testing, not recommended for production
        // Also weird thing: All browsers I tested actually store the username password after `WWW-Authenticate` challenge,
        // so it caused some trouble figuring out how the client is sending the `Authorization` header even though the
        // frontend code does not explicitly set it.
        http.httpBasic().disable();

        // Thoughts:
        // For form based login, you don't need CSRF protection.
        // It doesn't trigger any side effect other than logging in the user and the attacker won't have the username or password
        // If user is not logged in, that means the user does not have the cookie. So attacker isn't doing anything.
        // If user is logged in, what is the attacker trying to do with login form? Nothing will be affected.
        // CORS will protect the rest
        //
        // Actually above is almost correct, but wrong!
        // https://stackoverflow.com/questions/6412813/do-login-forms-need-tokens-against-csrf-attacks/15350123#15350123

        // TODO: Figure out if CSRF Token is still needed for REST API using Cookie Auth, this is not clear yet

        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        // Wasted some time here because POSTMAN decides to re-direct automatically with the same method, not GET
        http.formLogin().loginProcessingUrl("/auth/login").successHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }).failureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        });

        // TODO: REMOVE!!!!!!!!!!!!!!!
        http.csrf().disable();
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
//        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
//        configuration.setAllowedMethods(List.of("HEAD",
//                "GET", "POST", "PUT", "DELETE", "PATCH"));
        // setAllowCredentials(true) is important, otherwise:
        // The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.
//        configuration.setAllowCredentials(true);
        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
//        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
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

//    @Bean
//    public DataSource dataSource() {
//        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver");
//        dataSource.setUrl("jdbc:h2:mem:myDb;DB_CLOSE_DELAY=-1;NON_KEYWORDS=KEY,VALUE");
//        dataSource.setUsername("root");
//        dataSource.setPassword("password");
//        return dataSource;
//    }

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
//        Converter<LocalDateTime, ZonedDateTime> toZonedDateTime =
//                ctx -> ctx.getSource() == null ? null : ctx.getSource().atZone(ZoneId.of("UTC"));
//        modelMapper.addConverter(toZonedDateTime);
//        modelMapper.addMappings(new PropertyMap<Profile, Profile>() {
//            @Override
//            protected void configure() {
//                skip(destination.getId());
//                skip(destination.getProfilePictureS3URL());
//            }
//        });

//        modelMapper.typeMap(ChatMessage.class, ChatMessageDTO.class).addMappings(mapper -> {
//            mapper.map(src -> src.getSentBy().getUsername(),
//                    ChatMessageDTO::setUserName);
//            mapper.map(src -> src.getContent(),
//                    ChatMessageDTO::setMessage);
//            mapper.map(src -> src.getSentAt(),
//                    ChatMessageDTO::setSentAt);
//        });

        // TODO: Can't figure out to do this for a particular mapping only
        modelMapper.getConfiguration().setSkipNullEnabled(true);
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

//    @Bean
//    public RememberMeServices rememberMeServices(final UserDetailsService userDetailsService) {
//        final String SECRET_KEY = "uUx}k@cHAd;=}q@.)@Q6";
//        final PersistentTokenRepository persistentTokenRepository = new JdbcTokenRepositoryImpl();
//        final PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices =
//                new PersistentTokenBasedRememberMeServices(SECRET_KEY, userDetailsService, persistentTokenRepository);
//        return persistentTokenBasedRememberMeServices;
//    }

//    @Bean
//    public CommandLineRunner createUsers(final UserRepository userRepository) {
//        return (args) -> {
//            final User user = new User();
//            user.setUsername("jimhalpert");
//            user.setPassword("password");
//            final Profile profile = new Profile();
//            profile.setFirstName("Jim");
//            profile.setLastName("Halpert");
//            user.setUserProfile(profile);
//            userRepository.save(user);
//        };
//    }
}
