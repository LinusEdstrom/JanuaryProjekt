package com.Edstrom.repository;

import com.Edstrom.entity.RentableItemDTO;
import com.Edstrom.entity.Rental;

import java.util.List;

public interface RentalRepository {

    List<Rental> findAll();

    List<Rental>findAllActiveRentals();

    List<RentableItemDTO> findAvailableItems();

    void save(Rental rental);

    void remove(Rental rental);
}
