package com.edstrom.entity;


import javax.persistence.*;
import java.math.BigDecimal;

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

        @Column(name = "display_name", nullable = false, length = 100)
        private String displayName;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Rental getRental() {
            return rental;
        }

        public void setRental(Rental rental) {
            this.rental = rental;
        }

        public RentalType getRentalType() {
            return rentalType;
        }

        public void setRentalType(RentalType rentalType) {
            this.rentalType = rentalType;
        }

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public BigDecimal getPriceCharged() {
            return priceCharged;
        }

        public void setPriceCharged(BigDecimal priceCharged) {
            this.priceCharged = priceCharged;
        }

    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}





