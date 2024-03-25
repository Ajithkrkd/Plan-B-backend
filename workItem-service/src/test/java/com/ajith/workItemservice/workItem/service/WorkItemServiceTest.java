package com.ajith.workItemservice.workItem.service;

import com.ajith.workItemservice.exceptions.ResourceNotFountException;
import com.ajith.workItemservice.feign.project.ProjectFeignService;
import com.ajith.workItemservice.workItem.repository.WorkItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WorkItemServiceTest {

    @InjectMocks
    private WorkItemService workItemService;

    @Mock
    private WorkItemRepository workItemRepository;

    @Mock
    private ProjectFeignService projectFeignService;


    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks ( this );
    }


    @Test
    void createWorkItem ( ) {
        //Arrange

        String workItemCategory = "EPIC";
        String title = "sample work";
        String parentWorkItemId = "1";
        Long projectId = 1L;

        Mockito.when ( projectFeignService.isProjectExist ( projectId ) ).thenReturn ( true );



    }

}