package com.edstrom.repository;

import com.edstrom.entity.Member;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
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
    @Override
    public void save(Member newMember) {
        try (Session saveMemberSession = sessionFactory.openSession()) {
        Transaction memberTransaction = saveMemberSession.beginTransaction();
        saveMemberSession.persist(newMember);
        memberTransaction.commit();
        }
    }
    @Override
    public void delete(Member deleteMember) {
        try (Session deleteMemberSession = sessionFactory.openSession()) {
            Transaction memberTransaction = deleteMemberSession.beginTransaction();
            deleteMemberSession.delete(deleteMember);
            memberTransaction.commit();
        }
    }
        @Override
        public List<Member> searchMembers(String searchWord) {
            if (searchWord == null || searchWord.trim().isEmpty()) {
                return new ArrayList<>(); // tom lista
            }
            try (Session session = sessionFactory.openSession()) {
                String hql = "FROM Member m WHERE LOWER(m.name) LIKE :search";
                return session.createQuery(hql, Member.class)
                        .setParameter("search", "%" + searchWord.trim().toLowerCase() + "%")
                        .list();
            }
        }






}
