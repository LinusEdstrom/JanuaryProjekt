package com.edstrom.repository;


import com.edstrom.entity.Member;

import java.util.List;

public interface MemberRepository {

    List<Member> findAll();

    void save(Member member);

    void delete(Member member);
}
