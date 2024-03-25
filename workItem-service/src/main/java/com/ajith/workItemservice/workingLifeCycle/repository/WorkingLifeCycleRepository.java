package com.ajith.workItemservice.workingLifeCycle.repository;

import com.ajith.workItemservice.workingLifeCycle.entity.WorkingLifeCycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkingLifeCycleRepository extends JpaRepository< WorkingLifeCycle , Long > {
    List< WorkingLifeCycle> findAllByCreatedByAndIsDeletedFalse (Long userId);
}
