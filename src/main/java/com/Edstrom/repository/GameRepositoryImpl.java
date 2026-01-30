package com.Edstrom.repository;

import com.Edstrom.entity.Game;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class GameRepositoryImpl implements GameRepository {

    private final SessionFactory sessionFactory;

    public GameRepositoryImpl(SessionFactory sessionFactory){this.sessionFactory = sessionFactory;}

    @Override
    public void save(Game game){

        try(Session session = sessionFactory.openSession()){

            Transaction tx = session.beginTransaction();

            session.persist(game);

            tx.commit();
        }
    }


}
