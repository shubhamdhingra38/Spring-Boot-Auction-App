package com.example.auctionapp.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private ProfileDTO profile;
}
