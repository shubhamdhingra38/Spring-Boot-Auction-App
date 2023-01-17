package com.example.auctionapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCredentialsResponseDTO {
    @NotNull
    private String userName;
}
