package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Profile;
import com.example.auctionapp.domain.User;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;

public class TestUtils {
    private static Faker faker = new Faker();

    public static User createUser(final UserRepository userRepository) {
        final Profile profile = new Profile();
        profile.setFirstName(faker.name().firstName());
        profile.setLastName(faker.name().lastName());

        final User user = new User();
        user.setUsername(profile.getFirstName());
        user.setPassword(profile.getFirstName());

        user.setUserProfile(profile);
        userRepository.save(user);

        return user;
    }
}
