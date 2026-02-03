package com.Edstrom.repository;

import com.Edstrom.entity.Rental;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class RentalRepositoryImpl implements RentalRepository {

    private final SessionFactory sessionFactory;

    public RentalRepositoryImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Rental> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Rental", Rental.class).list();
        }
    }
    @Override
    public List<Rental> findAllActiveRentals(){
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT r FROM Rental r WHERE r.returnDate IS NULL",
                    Rental.class
                    ).getResultList();
        }
    }
    @Override
    public void save (Rental rental) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(rental);
            tx.commit();
        }
    }



}
