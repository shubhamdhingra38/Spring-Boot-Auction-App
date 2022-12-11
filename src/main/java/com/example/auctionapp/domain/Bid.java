package com.example.auctionapp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
// TODO: When using @ToString here, I was getting StackOverflow Error. Because of Auction. Excluding it works
@ToString(exclude = {"auction", "placedBy"})
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "placed_by_id")
    private User placedBy;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime placedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @PrePersist
    public void prePersistBid() {
        if (placedAt == null) {
            placedAt = LocalDateTime.now();
        }
    }
}
