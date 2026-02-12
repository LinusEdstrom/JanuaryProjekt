package com.edstrom.service;

import com.edstrom.entity.Member;
import com.edstrom.entity.Rental;
import com.edstrom.entity.RentedObject;
import com.edstrom.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class RentalServiceTest {

    private RentalRepository rentalRepository;
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        rentalRepository = mock(RentalRepository.class);
        rentalService = new RentalService(rentalRepository);
    }

    @Test
    void createRental_WithOneObjectRented_ShouldSave_RentingMember_OneObject_RentalDateNow_ReturnDateNull() {
        Member testMember = new Member();
        testMember.setId(5L);

        RentedObject object = new RentedObject();
        object.setPriceCharged(BigDecimal.valueOf(50));

        rentalService.createRental(testMember, Collections.singletonList(object));

        ArgumentCaptor<Rental> rentalCaptor = ArgumentCaptor.forClass(Rental.class);
        verify(rentalRepository, times(1)).save(rentalCaptor.capture());

        Rental savedRental = rentalCaptor.getValue();
        assertEquals(testMember, savedRental.getMember());
        assertEquals(1, savedRental.getRentedObjects().size());
        assertEquals(LocalDate.now(), savedRental.getRentalDate());
        assertNull(savedRental.getReturnDate());
    }
    @Test
    void returnRental_OneObjectFiveDays_SavesOneTime_GetTotalPrice_SetsReturnDate() {
        Rental testRental = new Rental();
        testRental.setId(5L);
        testRental.setRentalDate(LocalDate.now().minusDays(5));

        RentedObject object = new RentedObject();
        object.setPriceCharged(BigDecimal.valueOf(50));
        testRental.addRentedObject(object);

        doAnswer(invocation -> null).when(rentalRepository).save(any(Rental.class));
        // Måste ha doAnswer för att save returnerar en void

        BigDecimal totalPrice = rentalService.returnRental(testRental);

        verify(rentalRepository, times (1)).save(testRental);
        assertEquals(BigDecimal.valueOf(250), totalPrice);
        assertNotNull(testRental.getReturnDate());
    }

    @Test
    void findAvailableItems() {
    }
    @Test
    void findAllActiveRentals() {
    }
    @Test
    void findRentalsByMember() {
    }
}