package com.edstrom.entity;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rental_type", nullable = false)
    private RentalType rentalType = RentalType.MOVIE;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(length = 30)
    private String genre;

    @Column
    private int length;

    @Column(name = "base_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal basePrice;

    // Constructors
    protected Movie() {
    }

    public Movie(String title, String genre, int length, BigDecimal basePrice) {
        this.title = title;
        this.genre = genre;
        this.length = length;
        this.basePrice = basePrice;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String displayName() {
        return "[MOVIE] " + title + " (" + genre + "," +
                " " + length + " min)";

    }
}
