package com.example.auctionapp.websocketinterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{channelName}")
    public String chat(final Principal principal, @DestinationVariable final String channelName, final String body) {
        simpMessagingTemplate.convertAndSend("/topic/" + channelName, body);
        return "Hello, world!";
    }
}
