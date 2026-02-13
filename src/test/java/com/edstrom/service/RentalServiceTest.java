package com.edstrom.service;

import com.edstrom.dto.RentableItemDTO;
import com.edstrom.entity.Member;
import com.edstrom.entity.Rental;
import com.edstrom.entity.RentedObject;
import com.edstrom.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        List<RentedObject> rentedObjects = new ArrayList<>();
        rentedObjects.add(object);

        rentalService.createRental(testMember, rentedObjects);

        ArgumentCaptor<Rental> rentalCaptor = ArgumentCaptor.forClass(Rental.class);
        verify(rentalRepository, times(1)).save(rentalCaptor.capture());
        //Måste ha ArgumentCaptor för save är en void. Så man måste capture tillbakakaka

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
    void findAvailableItems_ShouldReturnListOf_AvailableItems() {
        List<RentableItemDTO> rentableItems = new ArrayList<>();
        rentableItems.add(mock(RentableItemDTO.class));

        when(rentalRepository.findAvailableItems()).thenReturn(rentableItems);

        List<RentableItemDTO> result = rentalService.findAvailableItems();

        assertEquals(1, result.size());
        verify(rentalRepository).findAvailableItems();

    }
    @Test
    void findRentalsByMember_ShouldReturnRentals() {
        Member testMember = new Member();
        testMember.setId(5L);

        List<Rental> rentals = new ArrayList<>();
        rentals.add(new Rental());

        when(rentalRepository.findByMember(testMember)).thenReturn(rentals);

        List<Rental> result = rentalService.findRentalsByMember(testMember);

        assertEquals(1, result.size());
        verify(rentalRepository).findByMember(testMember);
    }
    @Test
    void findAllActiveRentals() {
    }
}