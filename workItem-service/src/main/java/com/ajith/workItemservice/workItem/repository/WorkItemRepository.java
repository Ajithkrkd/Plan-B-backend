package com.ajith.workItemservice.workItem.repository;

import com.ajith.workItemservice.workItem.entity.WorkItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkItemRepository extends JpaRepository<Long, WorkItem > {
}
