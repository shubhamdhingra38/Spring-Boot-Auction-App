package com.example.auctionapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private String sentToUsername;
    private String sentByUsername;
    private LocalDateTime sentAt;
    private String content;
}
