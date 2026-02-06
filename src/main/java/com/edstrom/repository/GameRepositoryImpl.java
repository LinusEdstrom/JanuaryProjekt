package com.edstrom.repository;

import com.edstrom.entity.Game;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class GameRepositoryImpl implements GameRepository {

    private final SessionFactory sessionFactory;

    public GameRepositoryImpl(SessionFactory sessionFactory){this.sessionFactory = sessionFactory;}

    @Override
    public List<Game> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Game", Game.class).list();
        }
    }

    public void save(Game game){

        try(Session session = sessionFactory.openSession()){

            Transaction tx = session.beginTransaction();

            session.persist(game);

            tx.commit();
        }
    }


}
