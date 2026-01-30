package com.Edstrom.entity;




import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "rental")

public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDate rentalDate;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentedObject> rentedObjects;

    protected Rental(){}

    public Rental(Member member, LocalDate rentalDate){
        this.member = member;
        this.rentalDate = rentalDate;
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

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public List<RentedObject> getRentedObjects() {
        return rentedObjects;
    }

    public void setRentedObjects(List<RentedObject> rentedObjects) {
        this.rentedObjects = rentedObjects;
    }
}
