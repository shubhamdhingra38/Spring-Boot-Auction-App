package com.example.auctionapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false, targetEntity = User.class)
    private User sentBy;

    @ManyToOne(optional = false, targetEntity = User.class)
    private User sentTo;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(nullable = false, length = 1000)
    private String content;

    @PrePersist
    public void prePersist() {
        if(sentAt == null) {
            sentAt = LocalDateTime.now();
        }
    }
}
