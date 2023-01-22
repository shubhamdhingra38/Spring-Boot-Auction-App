package com.example.auctionapp.restinterface;

import com.example.auctionapp.dtos.PaginatedChatMessagesDTO;
import com.example.auctionapp.dtos.UserDTO;
import com.example.auctionapp.exceptions.UserNotFoundException;
import com.example.auctionapp.infra.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ChatHistoryController {

    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping("/chatHistory/{sentTo}")
    public PaginatedChatMessagesDTO getChatHistory(final Principal principal,
                                                   @PathVariable String sentTo,
                                                   @RequestParam(defaultValue = "0") int pageNumber) {
        try {
            return chatMessageService.getChatHistory(principal.getName(), sentTo, pageNumber);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }
    }

    @GetMapping("/chatHistoryUsers")
    public List<UserDTO> getChatHistoryUsers(final Principal principal) {
        return chatMessageService.getUsersWithChatHistory(principal.getName());
    }
}
