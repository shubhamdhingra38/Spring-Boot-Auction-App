//package com.example.auctionapp.domain;
//
//import javax.persistence.*;
//import java.util.Date;
//import java.util.List;
//
//@Entity
//public class Auction {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(nullable = false)
//    private String description;
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Item> items;
//
//    @Column(nullable = false)
//    private Date closingTime = new Date();
//
//    @OneToMany()
//    private
//}
