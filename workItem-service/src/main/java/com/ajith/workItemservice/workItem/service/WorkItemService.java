package com.ajith.workItemservice.workItem.service;

import com.ajith.workItemservice.exceptions.ResourceNotFountException;
import com.ajith.workItemservice.feign.project.ProjectFeignService;
import com.ajith.workItemservice.utils.BasicResponse;
import com.ajith.workItemservice.workItem.entity.WorkItem;
import com.ajith.workItemservice.workItem.enums.WorkItemCategory;
import com.ajith.workItemservice.workItem.enums.WorkItemState;
import com.ajith.workItemservice.workItem.repository.WorkItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkItemService implements IWorkItemService{

    private final ProjectFeignService projectFeignService;
    private final WorkItemRepository workItemRepository;

    @Override
    public ResponseEntity < BasicResponse > createWorkItem (
            String workItemCategory,
            String title,
            Optional < String > parentWorkItemId,
            String projectId,
            String authHeader) {

        try {

            boolean isExist = projectFeignService.isProjectExist (Long.valueOf(projectId));

                if(!isExist){
                    throw new ResourceNotFountException ( "the project with this " + projectId +" is not exist" );
                }

            createWorkItemAndSave ( workItemCategory, title, parentWorkItemId, projectId );
            return ResponseEntity.ok ( BasicResponse.builder ()
                    .message ( "WorkItem created successfully " )
                    .description ( "work item created successfully try to add more details in the work item " )
                    .timestamp ( LocalDateTime.now () )
                    .status ( HttpStatus.CREATED.value ( )
                    ).build ());


        } catch (ResourceNotFountException e) {
            throw new RuntimeException ( e );
        } catch (Exception e) {
            throw new RuntimeException ( e );
        }
    }

    @Override
    public ResponseEntity < BasicResponse > assignMemberToWorkItem (String workItemId, Long userId) {
        try {
            Optional<WorkItem> optionalWorkItem = workItemRepository.findById ( Long.valueOf (workItemId) );
            if(!optionalWorkItem.isPresent ( )) {
                throw new ResourceNotFountException ( "WorkItem does not exist with this work item id " + workItemId );
            }
            WorkItem existingWorkItem = optionalWorkItem.get ();
            existingWorkItem.getMembersAssigned ().add ( userId );
            workItemRepository.save ( existingWorkItem );
            return ResponseEntity.ok ( BasicResponse.builder()
                    .message ( "member assigned successfully " )
                    .description ( "member wih id "+ userId+ " to work item "+existingWorkItem.getTitle ())
                    .timestamp ( LocalDateTime.now () )
                    .status ( HttpStatus.OK.value ( ) )
                    .build());
        } catch (ResourceNotFountException e) {
            throw new RuntimeException ( e );
        } catch (Exception e) {
            throw new RuntimeException ( e );
        }
    }

    @Override
    public ResponseEntity < BasicResponse > changeWorkItemState (String workItemId, String newState) {
        try {
            Optional<WorkItem> optionalWorkItem = workItemRepository.findById ( Long.valueOf ( workItemId ) );
            if( !optionalWorkItem.isPresent()){
                throw new ResourceNotFountException ( "work item is not present with this work item id "+ workItemId );
            }
            WorkItem existingWorkItem = optionalWorkItem.get();
            WorkItemState  state = null;
            if(newState.equals("TODO")){
                state = WorkItemState.TODO;
            }
            if(newState.equals("DONE")){
                state = WorkItemState.DONE;
            }
            if(newState.equals("DOING")){
                state = WorkItemState.DOING;
            }
            existingWorkItem.setState ( state );
            workItemRepository.save ( existingWorkItem );
           return ResponseEntity.ok ( BasicResponse.builder ( )
                    .description ( "state has changed " + " to  " + state )
                    .message ( "work item state changed successfully" )
                    .status ( HttpStatus.OK.value ( ) )
                    .timestamp ( LocalDateTime.now ( ) )
                    .build ( )
            );
        } catch (NumberFormatException e) {
            throw new RuntimeException ( e );
        } catch (ResourceNotFountException e) {
            throw new RuntimeException ( e );
        }
    }

    private void createWorkItemAndSave (String workItemCategory, String title, Optional < String > parentWorkItemId, String projectId) {
        WorkItemCategory category = getWorkItemCategory ( workItemCategory );

        WorkItem newWorkItem = WorkItem.builder ()
                .parentWorkItemId ( parentWorkItemId.isPresent () ? Long.valueOf ( parentWorkItemId.get () ) : null )
                .title ( title)
                .createdAt ( new Date ( System.currentTimeMillis () ) )
                .state ( WorkItemState.TODO )
                .category ( category )
                .projectId ( Long.valueOf ( projectId ) )
                .build ();
        workItemRepository.save ( newWorkItem );
    }

    private static WorkItemCategory getWorkItemCategory (String workItemCategory) {
        WorkItemCategory category = null;
        if( workItemCategory.equals ( "EPIC" )){
            category = WorkItemCategory.EPIC;
        }
        if ( workItemCategory.equals ( "ISSUE" ) ){
            category = WorkItemCategory.ISSUE;
        }
        if( workItemCategory.equals ( "TASK" )){
            category = WorkItemCategory.TASK;
        }
        return category;
    }
}
