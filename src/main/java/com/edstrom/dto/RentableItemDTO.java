package com.edstrom.dto;

import com.edstrom.entity.RentalType;

import java.math.BigDecimal;

public class RentableItemDTO {

    private long id;
    private String displayName;
    private BigDecimal basePrice;
    private RentalType rentalType;
    

    public RentableItemDTO(long id, String displayName, BigDecimal basePrice, String rentalType) {
        this.id = id;
        this.displayName = displayName;
        this.basePrice = basePrice;
        this.rentalType = RentalType.valueOf(rentalType);

    }

    public long getId() { return id; }
    public String getDisplayName() { return displayName; }
    public BigDecimal getBasePrice() { return basePrice; }
    public RentalType getRentalType() { return rentalType;}


    @Override
    public String toString() {
        return "[" + rentalType + "] " + displayName + " | $" + basePrice;
    }


}