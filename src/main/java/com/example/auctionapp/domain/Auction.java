package com.example.auctionapp.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@JsonView(Views.Public.class)
@NoArgsConstructor
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
    private List<Item> items = new ArrayList<>();

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

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void addItem(Item item) {
        items.add(item);
    }
}
