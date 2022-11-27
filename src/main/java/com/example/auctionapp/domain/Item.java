package com.example.auctionapp.domain;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private double startingPrice;

    // many items can belong to one auction
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Auction.class)
    private Auction auction;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Category category;

    protected Item() {
    }

    public Item(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", name='" + name + '\'' + ", category=" + category + '}';
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

}
