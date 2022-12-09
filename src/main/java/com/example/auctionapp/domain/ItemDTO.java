package com.example.auctionapp.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ItemDTO {
    long id;
    String name;
    String description;
    double startingPrice;
    CategoryDTO category;
    long auctionId;
}
