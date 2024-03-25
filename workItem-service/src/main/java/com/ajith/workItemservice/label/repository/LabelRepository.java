package com.ajith.workItemservice.label.repository;

import com.ajith.workItemservice.label.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabelRepository extends JpaRepository< Label ,Long > {
    Optional< Label> findByLabelName (String labelTitle);
}
