package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}
