package com.example.auctionapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {
    @NotNull
    private String userName;
    @NotNull
    private String message;
}
