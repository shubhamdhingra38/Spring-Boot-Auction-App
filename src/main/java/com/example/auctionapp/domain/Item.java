package com.example.auctionapp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;


    private String description;

    private double startingPrice;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "auction_id")
    private Auction auction;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    private Category category;

    protected Item() {
    }

    public Item(String name, Category category) {
        this.name = name;
        this.category = category;
    }
}
