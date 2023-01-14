package com.example.auctionapp.restinterface;

import com.example.auctionapp.dtos.UserCreateRequestDTO;
import com.example.auctionapp.dtos.UserDTO;
import com.example.auctionapp.exceptions.UserNotFoundException;
import com.example.auctionapp.exceptions.UsernameExistsException;
import com.example.auctionapp.infra.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    public UserDTO getUserDetails(@PathVariable final Long userId) throws UserNotFoundException {
        final UserDTO userDTO = userService.getUserDetails(userId);
        return userDTO;
    }


}
