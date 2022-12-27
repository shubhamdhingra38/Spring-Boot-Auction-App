package com.example.auctionapp.infra;

import com.example.auctionapp.domain.User;
import com.example.auctionapp.dtos.UserDTO;
import com.example.auctionapp.exceptions.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    public UserDTO getUserDetails(final long userId) throws UserNotFoundException {
        final Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UserNotFoundException();
        }
        return modelMapper.map(user.get(), UserDTO.class);
    }
}
