package com.Edstrom.repository;

import com.Edstrom.entity.RentableItemDTO;
import com.Edstrom.entity.Rental;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
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
    public List<Rental> findAllActiveRentals() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "SELECT DISTINCT r FROM Rental r " +
                            "LEFT JOIN FETCH r.rentedObjects " +
                            "WHERE r.returnDate IS NULL",
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
    @Override
    public void remove (Rental rental) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(rental);
            tx.commit();
        }
    }
    public List<RentableItemDTO> findAvailableItems() {
        try (Session session = sessionFactory.openSession()) {

            List<RentableItemDTO> movies = session.createQuery(
                    "SELECT new com.Edstrom.dto.RentableItemDTO(m.id, m.name, m.basePrice, 'MOVIE') " +
                            "FROM Movie m " +
                            "WHERE m.id NOT IN (" +
                            "   SELECT ro.itemId FROM RentedObject ro " +
                            "   WHERE ro.rental.returnDate IS NULL" +
                            ")",
                    RentableItemDTO.class
            ).getResultList();

            List<RentableItemDTO> costumes = session.createQuery(
                    "SELECT new com.Edstrom.dto.RentableItemDTO(c.id, c.name, c.basePrice, 'COSTUME') " +
                            "FROM Costume c " +
                            "WHERE c.id NOT IN (" +
                            "   SELECT ro.itemId FROM RentedObject ro " +
                            "   WHERE ro.rental.returnDate IS NULL" +
                            ")",
                    RentableItemDTO.class
            ).getResultList();

            List<RentableItemDTO> games = session.createQuery(
                    "SELECT new com.Edstrom.dto.RentableItemDTO(g.id, g.name, g.basePrice, 'GAME') " +
                            "FROM Game g " +
                            "WHERE g.id NOT IN (" +
                            "   SELECT ro.itemId FROM RentedObject ro " +
                            "   WHERE ro.rental.returnDate IS NULL" +
                            ")",
                    RentableItemDTO.class
            ).getResultList();

            List<RentableItemDTO> allItems = new ArrayList<>();
            allItems.addAll(movies);
            allItems.addAll(costumes);
            allItems.addAll(games);

            return allItems;
        }
    }



}
