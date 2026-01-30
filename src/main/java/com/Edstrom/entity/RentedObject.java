package com.Edstrom.entity;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

    @Entity
    @Table(name = "rented_objects")
    public class RentedObject {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(optional = false)
        @JoinColumn(name = "rental_id", nullable = false)
        private Rental rental;

        @Enumerated(EnumType.STRING)
        @Column(name = "rental_type", nullable = false, length = 20)
        private RentalType rentalType;

        @Column(name = "item_id", nullable = false)
        private Long itemId;

        @Column(name = "price_charged", nullable = false, precision = 10, scale = 2)
        private BigDecimal priceCharged;

        @Column(name = "rental_date", nullable = false)
        private LocalDate rentalDate;

        @Column(name = "return_date")
        private LocalDate returnDate;
    }





