package com.edstrom.repository;

import com.edstrom.dto.RentableItemDTO;
import com.edstrom.entity.Member;
import com.edstrom.entity.Rental;

import java.util.List;

public interface RentalRepository {

    List<Rental> findAll();

    List<Rental>findAllActiveRentals();

    List<RentableItemDTO> findAvailableItems();

    List<Rental> findByMember(Member member);

    void save(Rental rental);

}
