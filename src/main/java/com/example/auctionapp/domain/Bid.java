package com.example.auctionapp.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
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

    @Column
    private String comment;

    @DecimalMin(value = "1.0", message = "Bid value is too small")
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
