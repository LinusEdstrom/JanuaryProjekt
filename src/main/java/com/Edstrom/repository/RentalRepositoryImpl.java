package com.Edstrom.repository;

import com.Edstrom.entity.Rental;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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



}
