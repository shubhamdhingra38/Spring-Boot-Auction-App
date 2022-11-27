package com.example.auctionapp.infra;

import com.example.auctionapp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom user model, save user to database
 * Spring doesn't persist the user in any database by default, it is all in memory
 * We need a Custom `UserDetails` and a custom `UserDetailsService`
 * NOTE: Adding the @Service annotation here is how Spring figures it out
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        CustomUserPrincipal customUser = new CustomUserPrincipal(user);
        return customUser;
    }
}
