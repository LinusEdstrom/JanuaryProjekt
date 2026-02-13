package com.edstrom.service;

import com.edstrom.entity.Member;
import com.edstrom.exception.InvalidEmailException;
import com.edstrom.exception.InvalidMemberDataException;
import com.edstrom.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MembershipServiceTest {

    private MemberRepository memberRepository;
    private MembershipService membershipService;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        membershipService = new MembershipService(memberRepository);
    }
    @Test
    void createMember_shouldSaveAndReturnMember() {
        Member resultMember = membershipService.createMember(" Olle ", "Olle@testmail.com");

        assertEquals("Olle", resultMember.getName());
        assertEquals("Olle@testmail.com", resultMember.getEmail());

        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void createMember_shouldThrow_InvalidMemberDataException_whenNameIs_Null() {
        assertThrows(InvalidMemberDataException.class,() ->
                membershipService.createMember(null, "Olle@testmail.com")
        );
        verify(memberRepository, never()).save(any());
    }
    @Test
    void createMember_shouldThrow_InvalidMemberDataException_whenNameIsEmpty() {
        assertThrows(InvalidMemberDataException.class,() ->
                membershipService.createMember("", "Olle@testmail.com")
        );
        verify(memberRepository, never()).save(any());
    }
    @Test
    void createMember_shouldThrow_InvalidEmailException_whenEmailIsNull(){
        assertThrows(InvalidEmailException.class,() ->
                membershipService.createMember("Olle", null)
        );
        verify(memberRepository, never()).save(any());
    }
    @Test
    void createMember_shouldThrow_InvalidEmailException_whenEmailIsEmpty(){
        assertThrows(InvalidEmailException.class, () ->
                membershipService.createMember("Olle", "")
        );
        verify(memberRepository, never()).save(any());
    }
    @Test
    void createMember_ShouldThrow_InvalidEmailException_whenEmailHasNo_SnabelA(){
        assertThrows(InvalidEmailException.class,() ->
                membershipService.createMember("Olle", "Invalid")
        );
        verify(memberRepository, never()).save(any());
    }
    @Test
    void deleteMember_ShouldDeleteMember_InRepository() {
        Member testMember = new Member("Olle", "Olle@testmail.com");

        membershipService.deleteMember(testMember);

        verify(memberRepository, times(1)).delete(testMember);
    }
    @Test
    void deleteMember_ShouldThrow_InvalidMemberDataException_WhenMemberIs_Null() {
        assertThrows(InvalidMemberDataException.class,() ->
                membershipService.deleteMember(null)
        );
        verify(memberRepository, never()).delete(any());
    }
    @Test
    void getAllMembers_ShouldGetAllMembers_FromRepository () {
        List<Member> allMockMembers = new ArrayList<>();
        allMockMembers.add(new Member("Olle", "Olle@testmail.com"));
        allMockMembers.add(new Member("Lisa", "Lisa@testmail.com"));
        allMockMembers.add(new Member("Anna", "Anna@testmail.com"));

        when(memberRepository.findAll()).thenReturn(allMockMembers);
        //att skicka den här mock listan kallas stubbing

        List<Member> result = membershipService.getAllMembers();

        assertEquals(3, result.size());
        assertEquals("Olle", result.get(0).getName());

        verify(memberRepository, times(1)).findAll();

    }



}