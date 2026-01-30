package com.Edstrom.repository;


import com.Edstrom.entity.Member;

import java.util.List;

public interface MemberRepository {

    List<Member> findAll();

    void save(Member member);

    void delete(Member member);
}
