package com.Edstrom.repository;

import com.Edstrom.entity.Costume;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class CostumeRepositoryImpl implements CostumeRepository {

    private final SessionFactory sessionFactory;

    public CostumeRepositoryImpl(SessionFactory sessionFactory){this.sessionFactory = sessionFactory;}

    @Override
    public void save(Costume costume){

        try(Session session = sessionFactory.openSession()){

            Transaction tx = session.beginTransaction();

            session.persist(costume);

            tx.commit();
        }
    }
}
