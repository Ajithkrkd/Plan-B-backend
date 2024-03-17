package com.ajith.projectservice.kafka.service;

import com.ajith.projectservice.entity.Project;
import com.ajith.projectservice.exceptions.ResourceNotFoundException;
import com.ajith.projectservice.exceptions.UserNotFoundException;
import com.ajith.projectservice.kafka.event.InviteMemberEvent;
import com.ajith.projectservice.members.dto.InviteRequest;
import com.ajith.projectservice.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private static long MEMBER_INVITATION_EXPIRATION =604800000;
    private final ProjectRepository projectRepository;

    public InviteMemberEvent createInviteMemberEvent(InviteRequest inviteRequest){
        try{String token = generateToken (  );
          return   InviteMemberEvent.builder ()
                    .email ( inviteRequest.getEmail () )
                    .message ( inviteRequest.getMessage () )
                    .token ( token )
                    .expirationTime ( new Date ( System.currentTimeMillis () + MEMBER_INVITATION_EXPIRATION ) )
                    .build ();

        }
        catch (Exception e){
            throw new RuntimeException (e.getMessage ());
        }
    }



    private String generateToken () {
        String token = createEmailVerificationToken ();
        return token;
    }

    private String createEmailVerificationToken () {
        return UUID.randomUUID ().toString ();
    }


}
