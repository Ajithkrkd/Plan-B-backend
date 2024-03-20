package com.ajith.projectservice.members.repository;

import com.ajith.projectservice.feign.dto.User;
import com.ajith.projectservice.members.entity.MemberInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository< MemberInvitation , Long > {
    Optional<MemberInvitation> findByInvitationToken (String token);

    List< MemberInvitation> findAllByInviteSentTo (String email);
}
