package com.example.auctionapp.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Getter
@Setter
@NoArgsConstructor
public class ProfileUpdateRequestDTO {
    private String firstName;
    private String lastName;
    private String description;
}
