package com.edstrom.entity;




import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "member")


public class Member {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false, length = 30)
    private String name;

@Column(nullable = false, unique = true, length = 30)
    private String email;

@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Rental> rentals;

public Member(){}

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public void addRental(Rental rental) {
    rentals.add(rental);
    rental.setMember(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getEmail(){
        return email;
        }
    public void setEmail(String email) {
        this.email = email;
        }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    @Override
    public String toString(){
    return name + " | " + email;
    }

}




