package com.example.auctionapp.infra;

import com.example.auctionapp.domain.Profile;
import com.example.auctionapp.domain.User;
import com.example.auctionapp.dtos.UserCreateRequestDTO;
import com.example.auctionapp.dtos.UserDTO;
import com.example.auctionapp.exceptions.UserNotFoundException;
import com.example.auctionapp.exceptions.UsernameExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO getUserDetails(final long userId) throws UserNotFoundException {
        final Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UserNotFoundException();
        }
        return modelMapper.map(user.get(), UserDTO.class);
    }

    public void createUser(final UserCreateRequestDTO userCreateRequest) throws UsernameExistsException {
        if (userRepository.existsByUsername(userCreateRequest.getUserName())) {
            throw new UsernameExistsException();
        }
        final User user = new User();
        user.setUsername(userCreateRequest.getUserName());
        user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        final Profile profile = new Profile();
        profile.setFirstName(userCreateRequest.getFirstName());
        profile.setLastName(userCreateRequest.getLastName());
        user.setUserProfile(profile);
        userRepository.save(user);
    }
}
