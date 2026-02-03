package com.Edstrom.entity;




import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentedObject> rentedObjects = new ArrayList<>();

    @Column(name = "rental_date", nullable = false)
    private LocalDate rentalDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;



    public Rental() {
        this.rentedObjects = new ArrayList<>();
    }

    public Rental (Member member, LocalDate rentalDate){
        this.member = member;
        this.rentalDate = rentalDate;
    }

    public void addRentedObject(RentedObject rentedObject) {
        rentedObjects.add(rentedObject);
        rentedObject.setRental(this);
    }
    public void calculateTotalPrice() {
        if (rentalDate == null || returnDate == null) {
            this.totalPrice = BigDecimal.ZERO;
            return;
        }
        long days = ChronoUnit.DAYS.between(rentalDate, returnDate);
        long chargeDays = days <= 0 ? 1 : days;
        //if(days <= 0) days = 1; else days;

        this.totalPrice = rentedObjects.stream()
                .map(ro -> ro.getPriceCharged().multiply(BigDecimal.valueOf(chargeDays)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
        // Antar jag kör på att man returnerar allt i en rental bara

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<RentedObject> getRentedObjects() {
        return rentedObjects;
    }

    public void setRentedObjects(List<RentedObject> rentedObjects) {
        this.rentedObjects = rentedObjects;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    @Override
    public String toString() {
        String memberName = (member != null)
                ? member.getName()
                : "Unknown member";

        int numberOfItems = (rentedObjects != null)
                ? rentedObjects.size()
                : 0;

        return memberName + " | Items rented: " + numberOfItems;
    }

}