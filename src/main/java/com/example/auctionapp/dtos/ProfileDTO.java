package com.example.auctionapp.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ProfileDTO {
    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String description;
    private String profilePictureURL;
}
