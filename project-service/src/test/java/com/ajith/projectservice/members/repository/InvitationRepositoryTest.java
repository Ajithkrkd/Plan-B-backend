package com.ajith.projectservice.members.repository;

import com.ajith.projectservice.members.entity.MemberInvitation;
import com.ajith.projectservice.members.enums.InvitationStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InvitationRepositoryTest {

    @Autowired
    private InvitationRepository invitationRepository;

    @AfterEach
    void teardown() throws Exception {
        invitationRepository.deleteAll ();
    }

    @Test
    @DisplayName ( "this will return the invitation details by th invitation token" )
    void findByInvitationToken ( ) {
        //given
        String invitationToken = UUID.randomUUID ().toString ();
        String invitationToken2 = UUID.randomUUID ().toString ();
        MemberInvitation invitation = MemberInvitation.builder ( )
                .inviteFrom ( null )
                .invitationMessage ( "please join our team to work together" )
                .inviteSentTo ( "ajith@gmail.com" )
                .createdAt (new Date ( System.currentTimeMillis () ) )
                .status ( InvitationStatus.PENDING )
                .invitationToken ( invitationToken )
                .build ( );

        invitationRepository.save ( invitation );
        //when
        MemberInvitation memberInvitation = invitationRepository.findByInvitationToken ( invitationToken ).get ();

        //then
        assertNotNull ( memberInvitation );
        assertEquals ( invitationToken, memberInvitation.getInvitationToken () );
        assertTrue ( memberInvitation.getId () > 0 );

    }

    @Test
    @DisplayName ( "this method will return all the invitation by the inviteSentToEmail " )
    void findAllMemberInvitationsByInviteSentTo(){
        //given
        String invitationToken2 = UUID.randomUUID ().toString ();
        MemberInvitation invitation = MemberInvitation.builder ( )
                .inviteFrom ( "jishnu@gmail.com" )
                .invitationMessage ( "please join our team to work together" )
                .inviteSentTo ( "ajith@gmail.com" )
                .createdAt (new Date ( System.currentTimeMillis () ) )
                .status ( InvitationStatus.PENDING )
                .invitationToken ( invitationToken2 )
                .build ( );
        MemberInvitation invitation2 = MemberInvitation.builder ( )
                .inviteFrom ( "ajith2255@gmail.com" )
                .invitationMessage ( "please join our team to work together" )
                .inviteSentTo ( "ajith@gmail.com" )
                .createdAt (new Date ( System.currentTimeMillis () ) )
                .status ( InvitationStatus.PENDING )
                .invitationToken ( invitationToken2 )
                .build ( );
        invitationRepository.save ( invitation );
        invitationRepository.save ( invitation2 );
        //when
        List <MemberInvitation> invitationList = invitationRepository.findAllByInviteSentTo ( "ajith@gmail.com" );
        //then
        assertTrue ( invitationList.size() == 2 );
        assertNotNull ( invitationList );
        assertNotNull ( "ajith@gmail.com",invitationList.get ( 0 ).getInviteSentTo () );

    }
}