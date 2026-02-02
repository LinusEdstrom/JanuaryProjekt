package com.Edstrom.entity;




import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rentals")
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

    public Rental() {}

    public Rental (Member member, LocalDate rentalDate){
        this.member = member;
        this.rentalDate = rentalDate;
    }

    public void addRentedObject(RentedObject rentedObject) {
        rentedObjects.add(rentedObject);
        rentedObject.setRental(this);
    }
        // Antar jag kör på att man returnerar allt i en rental bara

    public void markAsReturned() {
        this.returnDate = LocalDate.now();
        for (RentedObject obj : rentedObjects) {
            obj.markAsReturned();
        }
    }

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

}