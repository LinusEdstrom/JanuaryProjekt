package com.Edstrom.repository;

import com.Edstrom.entity.Movie;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class MovieRepositoryImpl implements MovieRepository {

    private final SessionFactory sessionFactory;

    public MovieRepositoryImpl(SessionFactory sessionFactory){this.sessionFactory = sessionFactory;}

    @Override
    public void save(Movie movie){

        try(Session session = sessionFactory.openSession()){

            Transaction tx = session.beginTransaction();

            session.persist(movie);

            tx.commit();
        }
    }



}
