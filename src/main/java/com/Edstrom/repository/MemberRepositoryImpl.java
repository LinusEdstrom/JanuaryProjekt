package com.Edstrom.repository;

import com.Edstrom.entity.Member;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class MemberRepositoryImpl implements MemberRepository {

    private final SessionFactory sessionFactory;

    public MemberRepositoryImpl(SessionFactory sessionFactory){this.sessionFactory = sessionFactory;}

    @Override
    public List<Member> findAll() {
     try (Session session = sessionFactory.openSession()) {
         return session.createQuery("FROM Member", Member.class).list();
     }
    }


    public void save(Member newMember) {
        try (Session saveMemberSession = sessionFactory.openSession()) {
        Transaction memberTransaction = saveMemberSession.beginTransaction();
        saveMemberSession.persist(newMember);
        memberTransaction.commit();
        }
    }
    public void delete(Member deleteMember) {
        try(Session deleteMemberSession = sessionFactory.openSession()) {
            Transaction memberTransaction = deleteMemberSession.beginTransaction();
            deleteMemberSession.delete(deleteMember);
            memberTransaction.commit();
        }
    }



}
