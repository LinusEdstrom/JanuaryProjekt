package com.Edstrom.service;

import com.Edstrom.entity.*;
import com.Edstrom.exception.createRentalError;
import com.Edstrom.repository.RentalRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

        rentalRepository.remove(rental);

        return totalPrice;
    }
    public List<RentableItemDTO> findAvailableItems() {
        return rentalRepository.findAvailableItems();
    }







}




