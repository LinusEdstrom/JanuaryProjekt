package com.edstrom.service;

import com.edstrom.dto.RentableItemDTO;
import com.edstrom.entity.*;
import com.edstrom.repository.RentalRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository){
        this.rentalRepository = rentalRepository;
    }

    public List<Rental> findAllActiveRentals() {
        return rentalRepository.findAllActiveRentals();
    }

    public void createRental(Member member, List<RentedObject> rentedObjects) {
        Rental rental = new Rental();
        rental.setMember(member);
        rental.setRentalDate(LocalDate.now());

        rental.setReturnDate(null);

        for (RentedObject ro : rentedObjects) {
            rental.addRentedObject(ro);
        }

        rentalRepository.save(rental);
    }
    public BigDecimal returnRental(Rental rental) {

        rental.setReturnDate(LocalDate.now());

        BigDecimal totalPrice = rental.getRentedObjects().stream()
                .map(RentedObject::getPriceCharged)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        rental.setTotalPrice(totalPrice);

        rentalRepository.save(rental);

        return totalPrice;
    }
    public List<RentableItemDTO> findAvailableItems() {
        return rentalRepository.findAvailableItems();
    }
    public List<Rental> getRentalsByMember(Member member) {
        return rentalRepository.findByMember(member);
    }




}




