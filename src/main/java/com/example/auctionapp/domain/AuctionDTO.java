package com.example.auctionapp.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
public class AuctionDTO {
    long id;
    String name;
    String description;
    List<ItemDTO> items;
    LocalDate closingTime;
    long createdById;
}
