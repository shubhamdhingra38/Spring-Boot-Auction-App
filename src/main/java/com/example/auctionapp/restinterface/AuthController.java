package com.example.auctionapp.restinterface;

import com.example.auctionapp.dtos.UserCreateRequestDTO;
import com.example.auctionapp.exceptions.UsernameExistsException;
import com.example.auctionapp.infra.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/verifyCredentials")
    public void isUserAuthenticated() {
    }

    @PostMapping("/register")
    public void createUser(@Valid @RequestBody final UserCreateRequestDTO userRequest) throws UsernameExistsException {
        userService.createUser(userRequest);
    }
}
