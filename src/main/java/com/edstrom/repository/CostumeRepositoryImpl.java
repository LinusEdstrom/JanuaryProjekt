package com.edstrom.repository;

import com.edstrom.entity.Costume;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class CostumeRepositoryImpl implements CostumeRepository {

    private final SessionFactory sessionFactory;

    public CostumeRepositoryImpl(SessionFactory sessionFactory){this.sessionFactory = sessionFactory;}

    @Override
    public List<Costume> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Costume", Costume.class).list();
        }
    }

    public void save(Costume costume){

        try(Session session = sessionFactory.openSession()){

            Transaction tx = session.beginTransaction();

            session.persist(costume);

            tx.commit();
        }
    }
}
