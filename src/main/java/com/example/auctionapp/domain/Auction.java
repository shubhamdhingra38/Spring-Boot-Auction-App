package com.example.auctionapp.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@JsonView(Views.Public.class)
@Entity
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // TODO: What is nullable? DB Schema constraint or just at runtime?
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    // one auction can have many items
    // TODO: Why does spring create an intermediate join table?
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items;

    @Column
    private LocalDate closingTime = LocalDate.now().plusDays(7);

    // TODO: Can't get nullable=false to work in the database schema
    @JsonView(Views.Internal.class)
    @ManyToOne(optional = false, targetEntity = User.class)
    private User createdBy;

    public Auction(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Auction() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public LocalDate getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalDate closingTime) {
        this.closingTime = closingTime;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
