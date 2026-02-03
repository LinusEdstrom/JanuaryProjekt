package com.Edstrom.service;

import com.Edstrom.entity.*;
import com.Edstrom.exception.createRentalError;
import com.Edstrom.repository.RentalRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository){
        this.rentalRepository = rentalRepository;
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



}




