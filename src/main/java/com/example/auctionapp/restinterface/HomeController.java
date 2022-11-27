package com.example.auctionapp.restinterface;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Keeping as unsecured controller
 */
@RestController
public class HomeController {


    @GetMapping("/home")
    public String getGreeting() {
        return "Hello, world!";
    }
}
