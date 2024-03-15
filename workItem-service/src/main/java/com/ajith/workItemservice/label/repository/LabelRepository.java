package com.ajith.workItemservice.label.repository;

import com.ajith.workItemservice.label.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository< Label ,Long > {
}
