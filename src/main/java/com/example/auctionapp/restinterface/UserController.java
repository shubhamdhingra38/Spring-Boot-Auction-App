package com.example.auctionapp.restinterface;

import com.example.auctionapp.dtos.UserCreateRequestDTO;
import com.example.auctionapp.dtos.UserDTO;
import com.example.auctionapp.exceptions.UserNotFoundException;
import com.example.auctionapp.exceptions.UsernameExistsException;
import com.example.auctionapp.infra.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

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

    @PostMapping("/user")
    public void createUser(@Valid @RequestBody final UserCreateRequestDTO userRequest) throws UsernameExistsException {
        userService.createUser(userRequest);
    }

//    @PostMapping("/login")
//    public void login(final Principal principal) {
//        System.out.println(principal.getName() + " logged in");
//    }

}
