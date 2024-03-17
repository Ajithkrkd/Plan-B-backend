package com.ajith.projectservice.members.repository;

import com.ajith.projectservice.members.entity.MemberInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository< MemberInvitation , Long > {
}
