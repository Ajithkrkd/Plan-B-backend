package com.ajith.workItemservice.label.service;

import com.ajith.workItemservice.label.repository.LabelRepository;
import com.ajith.workItemservice.workItem.entity.WorkItem;
import com.ajith.workItemservice.workItem.enums.WorkItemCategory;
import com.ajith.workItemservice.workItem.enums.WorkItemState;
import com.ajith.workItemservice.workItem.repository.WorkItemRepository;
import com.ajith.workItemservice.workingLifeCycle.entity.WorkingLifeCycle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LabelServiceTest {

    @Mock
    private WorkItemRepository workItemRepository;

    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    LabelService labelService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks ( this );
    }
    @Test
    void createAndAddLabelToWorkItem ( ) {

        Long workItemId = 1L;
        String labelTitle = "design";

        WorkItem expectedWorkItem = WorkItem.builder ( )
                .workItemId ( 1L )
                .createdAt ( new Date (System.currentTimeMillis ()) )
                .parentWorkItemId ( null )
                .title ( "homepage" )
                .state ( WorkItemState.TODO )
                .labels ( new ArrayList <> () )
                .category ( WorkItemCategory.EPIC )
                .projectId ( 1L )
                .comments ( new ArrayList<> () )
                .workingLifeCycle (null)
                .isDeleted ( false )
                .membersAssigned ( null )
                .isArchived ( false )
                .build ( );

        //when
        when ( workItemRepository.findById (workItemId) ).thenReturn ( Optional.ofNullable ( expectedWorkItem ) );
        when ( labelRepository.findByLabelName ( labelTitle ) ).thenReturn ( Optional.ofNullable ( null) );

    }



}