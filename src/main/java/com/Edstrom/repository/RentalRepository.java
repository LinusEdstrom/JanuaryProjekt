package com.Edstrom.repository;

import com.Edstrom.entity.Rental;

import java.util.List;

public interface RentalRepository {

    List<Rental> findAll();

    List<Rental>findAllActiveRentals();


    void save(Rental rental);
}
