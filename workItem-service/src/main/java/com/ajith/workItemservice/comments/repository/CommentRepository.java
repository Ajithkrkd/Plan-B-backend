package com.ajith.workItemservice.comments.repository;

import com.ajith.workItemservice.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository< Comment ,Long > {
}
