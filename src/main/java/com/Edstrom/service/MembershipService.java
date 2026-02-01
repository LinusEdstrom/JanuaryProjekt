package com.Edstrom.service;

import com.Edstrom.entity.Member;
import com.Edstrom.exception.InvalidEmailException;
import com.Edstrom.exception.InvalidMemberDataException;
import com.Edstrom.repository.MemberRepository;

public class MembershipService {

    private final MemberRepository memberRepository;

    public MembershipService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    public Member createMember(String name, String email) {
        if (name == null || name.isEmpty()) throw new InvalidMemberDataException(" please write a proper name");
        if (email == null || email.isEmpty() || !email.contains("@"))
            throw new InvalidEmailException (" please write a proper email");

        Member newMember = new Member(name.trim(), email.trim());
        memberRepository.save(newMember);
        return newMember;
    }



}
