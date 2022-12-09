package com.example.auctionapp.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


@Setter
@Getter
@JsonView(Views.Public.class)
@NoArgsConstructor
@ToString
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

    @OneToOne(mappedBy = "auction", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Item item;

    @Column(nullable = false)
    private LocalDateTime closingTime;

    // TODO: Can't get nullable=false to work in the database schema
    @JsonView(Views.Internal.class)
    @ManyToOne(optional = false, targetEntity = User.class)
    private User createdBy;

    public Auction(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @PrePersist
    public void prePersist() {
        if(closingTime == null) {
            closingTime = LocalDateTime.now().plusDays(7);
        }
    }

}
