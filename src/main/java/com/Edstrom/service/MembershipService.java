package com.Edstrom.service;

import com.Edstrom.entity.Member;
import com.Edstrom.repository.MemberRepository;

public class MembershipService {

    private final MemberRepository memberRepository;

    public MembershipService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    public Member createMember(String name, String email) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException(" please write a name");
        if (email == null || email.isEmpty()) throw new IllegalArgumentException(" please write an email");

        Member newMember = new Member(name.trim(), email.trim());
        memberRepository.save(newMember);
        return newMember;
    }



}
