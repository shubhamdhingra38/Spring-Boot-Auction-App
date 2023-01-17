package com.example.auctionapp.websocketinterface;

import com.example.auctionapp.dtos.MessageResponseDTO;
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
    public void chat(final Principal principal, @DestinationVariable final String channelName, final String body) {
        final MessageResponseDTO messageResponseDTO = MessageResponseDTO.builder().userName(principal.getName()).message(body).build();
        simpMessagingTemplate.convertAndSend("/topic/" + channelName, messageResponseDTO);
    }
}
