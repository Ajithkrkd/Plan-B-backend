package com.ajith.projectservice.repository;

import com.ajith.projectservice.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository< Project ,Long > {
}
