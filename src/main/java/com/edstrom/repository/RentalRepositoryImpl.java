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

            if(rental.getId() == null){
                session.persist(rental);
            }else {
                session.merge(rental);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }
    @Override
    public List<Rental> findByMember(Member member) {

        if (member == null || member.getId() == null) {
            return Collections.emptyList();
        }

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT r FROM Rental r " +
                                    "LEFT JOIN FETCH r.rentedObjects " +
                                    "WHERE r.member.id = :memberId " +
                                    "ORDER BY r.rentalDate DESC, r.returnDate DESC",
                            Rental.class
                    )
                    .setParameter("memberId", member.getId())
                    .list();

        } catch (RuntimeException e) {
            throw e;
        }
    }


    @Override
    public List<RentableItemDTO> findAvailableItems() {
        try (Session session = sessionFactory.openSession()) {

            List<RentableItemDTO> result = new ArrayList<>();

            // Movies
            result.addAll(
                    session.createQuery(
                            "SELECT new com.edstrom.dto.RentableItemDTO(" +
                                    "m.id, m.title, m.basePrice, 'MOVIE'" +
                                    ") " +
                                    "FROM Movie m " +
                                    "WHERE NOT EXISTS (" +
                                    "   SELECT 1 FROM RentedObject ro " +
                                    "   JOIN ro.rental r " +
                                    "   WHERE ro.itemId = m.id " +
                                    "     AND ro.rentalType = com.edstrom.entity.RentalType.MOVIE " +
                                    "     AND r.returnDate IS NULL" +
                                    ")",
                            RentableItemDTO.class
                    ).getResultList()
            );

            // Costumes
            result.addAll(
                    session.createQuery(
                            "SELECT new com.edstrom.dto.RentableItemDTO(" +
                                    "c.id, c.description, c.basePrice, 'COSTUME'" +
                                    ") " +
                                    "FROM Costume c " +
                                    "WHERE NOT EXISTS (" +
                                    "   SELECT 1 FROM RentedObject ro " +
                                    "   JOIN ro.rental r " +
                                    "   WHERE ro.itemId = c.id " +
                                    "     AND ro.rentalType = com.edstrom.entity.RentalType.COSTUME " +
                                    "     AND r.returnDate IS NULL" +
                                    ")",
                            RentableItemDTO.class
                    ).getResultList()
            );

            // Games
            result.addAll(
                    session.createQuery(
                            "SELECT new com.edstrom.dto.RentableItemDTO(" +
                                    "g.id, g.name, g.basePrice, 'GAME'" +
                                    ") " +
                                    "FROM Game g " +
                                    "WHERE NOT EXISTS (" +
                                    "   SELECT 1 FROM RentedObject ro " +
                                    "   JOIN ro.rental r " +
                                    "   WHERE ro.itemId = g.id " +
                                    "     AND ro.rentalType = com.edstrom.entity.RentalType.GAME " +
                                    "     AND r.returnDate IS NULL" +
                                    ")",
                            RentableItemDTO.class
                    ).getResultList()
            );

            return result;
        }
    }




}





