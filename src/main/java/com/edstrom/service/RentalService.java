package com.edstrom.service;

import com.edstrom.dto.RentableItemDTO;
import com.edstrom.entity.*;
import com.edstrom.exception.RentalErrorException;
import com.edstrom.repository.RentalRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RentalService {

    private final RentalRepository rentalRepository;

    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<Rental> findAllActiveRentals() {
        return rentalRepository.findAllActiveRentals();
    }

    public void createRental(Member member, List<RentedObject> rentedObjects) {
        if (member == null || member.getId() == null) {
            throw new RentalErrorException("Missing or invalid member");
        }
        if (rentedObjects == null || rentedObjects.isEmpty()) {
            throw new RentalErrorException("No or invalid items selected for renting");
        }
        Rental rental = new Rental();
        rental.setMember(member);
        rental.setRentalDate(LocalDate.now());
        rental.setReturnDate(null);

        for (RentedObject ro : rentedObjects) {
            if (ro == null) {
                throw new RentalErrorException("Invalid rented object/objects");
            }
            rental.addRentedObject(ro);
        }
        try {
            rentalRepository.save(rental);      // Gör en egen try/catch mot repository.
        } catch (Exception e) {
            throw new RentalErrorException("Could not save rental" + e);
        }
    }
        public BigDecimal returnRental (Rental rental){

            if (rental == null || rental.getId() == null) {
                throw new RentalErrorException("Missing or invalid rental");
            }

            if (rental.getReturnDate() != null) {
                throw new RentalErrorException("Rental is already returned");
            }

            if (rental.getRentedObjects() == null || rental.getRentedObjects().isEmpty()) {
                throw new RentalErrorException("Rental contains no objects");
            }
            if (rental.getRentalDate() == null) {
                throw new RentalErrorException("Rental date is missing");
            }
            LocalDate returnDate = LocalDate.now();
            rental.setReturnDate(returnDate);

            BigDecimal totalPrice = calculateTotalPrice(rental);
            rental.setTotalPrice(totalPrice);

            try {
                rentalRepository.save(rental);
            } catch (Exception e) {
                throw new RentalErrorException("Error updating rental: " + e.getMessage());
            }
            return totalPrice;
        }
    private BigDecimal calculateTotalPrice(Rental rental) {

        long days = ChronoUnit.DAYS.between(
                rental.getRentalDate(),
                rental.getReturnDate()
        );
        if (days < 0) {
            throw new RentalErrorException("Return date cannot be before rental date");
        }
        long chargeDays = days == 0 ? 1 : days;

        return rental.getRentedObjects().stream()
                .map(ro -> ro.getPriceCharged()
                        .multiply(BigDecimal.valueOf(chargeDays)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

        public List<RentableItemDTO> findAvailableItems () {
            return rentalRepository.findAvailableItems();
        }
        public List<Rental> findRentalsByMember (Member member) {
            if (member == null || member.getId() == null) {
                throw new RentalErrorException("Missing or invalid member");
            }
            try {
                return rentalRepository.findByMember(member);
            } catch (Exception e) {
                throw new RentalErrorException("Could not load rental history for: "
                        + member.getName() + e.getMessage());
            }
        }
}







