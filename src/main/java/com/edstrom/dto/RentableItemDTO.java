package com.edstrom.dto;

import com.edstrom.entity.RentalType;

import java.math.BigDecimal;

public class RentableItemDTO {

    private Long id;
    private String displayName;
    private BigDecimal basePrice;
    private RentalType rentalType;

    public RentableItemDTO(Long id, String displayName, BigDecimal basePrice, RentalType rentalType) {
        this.id = id;
        this.displayName = displayName;
        this.basePrice = basePrice;
        this.rentalType = rentalType;
    }

    public Long getId() { return id; }
    public String getDisplayName() { return displayName; }
    public BigDecimal getBasePrice() { return basePrice; }
    public RentalType getRentalType() { return rentalType; }


    @Override
    public String toString() {
        return displayName + " | $" + basePrice;
    }
}