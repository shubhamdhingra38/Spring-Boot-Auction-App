package com.example.auctionapp.infra;

import com.example.auctionapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
    Boolean existsByUsername(String username);
}
