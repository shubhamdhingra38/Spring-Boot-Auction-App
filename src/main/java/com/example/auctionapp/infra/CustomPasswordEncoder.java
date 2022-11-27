package com.example.auctionapp.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Don't know what the fuck is this, for hashing the password maybe? Let's not hash for now
 */
@Component
public class CustomPasswordEncoder {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
