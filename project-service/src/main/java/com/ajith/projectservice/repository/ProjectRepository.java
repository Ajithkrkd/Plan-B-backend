package com.ajith.projectservice.repository;

import com.ajith.projectservice.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository< Project ,Long > {
    List< Project> findByProjectRootAdministrator (String userName);

    boolean existsByTitle (String title);
}
