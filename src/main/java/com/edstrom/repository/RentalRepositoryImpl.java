package com.edstrom.repository;

import com.edstrom.dto.RentableItemDTO;
import com.edstrom.entity.Member;
import com.edstrom.entity.Rental;
import com.edstrom.entity.RentalType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RentalRepositoryImpl implements RentalRepository {

    private final SessionFactory sessionFactory;

    public RentalRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public List<Rental> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM Rental",
                    Rental.class
            ).getResultList();
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
    public void save(Rental rental) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(rental);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void remove(Rental rental) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.remove(rental);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
    @Override
    public List<RentableItemDTO> findAvailableItems() {
        try (Session session = sessionFactory.openSession()) {

            List<RentableItemDTO> result = new ArrayList<>();

            result.addAll(findAvailableMovies(session));
            result.addAll(findAvailableCostumes(session));
            result.addAll(findAvailableGames(session));

            return result;
        }
    }

    private List<RentableItemDTO> findAvailableMovies(Session session) {
        Query<RentableItemDTO> query = session.createQuery(
                "SELECT new com.edstrom.dto.RentableItemDTO(" +
                        "m.id, m.title, m.basePrice, :type" +
                        ") " +
                        "FROM Movie m " +
                        "WHERE NOT EXISTS (" +
                        "SELECT 1 FROM RentedObject ro " +
                        "WHERE ro.itemId = m.id " +
                        "AND ro.rental.returnDate IS NULL" +
                        ")",
                RentableItemDTO.class
        );
        query.setParameter("type", RentalType.MOVIE);
        return query.getResultList();
    }

    private List<RentableItemDTO> findAvailableCostumes(Session session) {
        Query<RentableItemDTO> query = session.createQuery(
                "SELECT new com.edstrom.dto.RentableItemDTO(" +
                        "c.id, c.description, c.basePrice, :type" +
                        ") " +
                        "FROM Costume c " +
                        "WHERE NOT EXISTS (" +
                        "SELECT 1 FROM RentedObject ro " +
                        "WHERE ro.itemId = c.id " +
                        "AND ro.rental.returnDate IS NULL" +
                        ")",
                RentableItemDTO.class
        );
        query.setParameter("type", RentalType.COSTUME);
        return query.getResultList();
    }

    private List<RentableItemDTO> findAvailableGames(Session session) {
        Query<RentableItemDTO> query = session.createQuery(
                "SELECT new com.edstrom.dto.RentableItemDTO(" +
                        "g.id, g.name, g.basePrice, :type" +
                        ") " +
                        "FROM Game g " +
                        "WHERE NOT EXISTS (" +
                        "SELECT 1 FROM RentedObject ro " +
                        "WHERE ro.itemId = g.id " +
                        "AND ro.rental.returnDate IS NULL" +
                        ")",
                RentableItemDTO.class
        );
        query.setParameter("type", RentalType.GAME);
        return query.getResultList();
    }
    public List<Rental> findByMember(Member member) {
        if (member == null || member.getId() == null) {
            return Collections.emptyList();
        }

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT r FROM Rental r " +
                                    "LEFT JOIN FETCH r.rentedObjects " +
                                    "WHERE r.member.id = :memberId " +
                                    "ORDER BY r.rentalDate DESC, r.returnDate DESC", Rental.class)
                    .setParameter("memberId", member.getId())
                    .list();
        } catch (Exception e) {
            e.printStackTrace(); // log full exception
            return Collections.emptyList();
        }
    }



}

