package com.edstrom.service;
import com.edstrom.entity.Member;
import com.edstrom.exception.InvalidEmailException;
import com.edstrom.exception.InvalidMemberDataException;
import com.edstrom.repository.MemberRepository;

import java.util.List;

public class MembershipService {

    private final MemberRepository memberRepository;

    public MembershipService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public List<Member> getAllMembers() {
        List<Member> list = memberRepository.findAll();
        return list;
    }

    public Member createMember(String name, String email) {
        if (name == null || name.isEmpty()) throw new InvalidMemberDataException(" please write a proper name");
        if (email == null || email.isEmpty() || !email.contains("@"))
            throw new InvalidEmailException (" please write a proper email");

        Member newMember = new Member(name.trim(), email.trim());
        memberRepository.save(newMember);
        return newMember;
    }
    public void deleteMember(Member member) {
        if (member == null) {
            throw new InvalidMemberDataException("Select a member to delete");
        }
        memberRepository.delete(member);
    }

}




