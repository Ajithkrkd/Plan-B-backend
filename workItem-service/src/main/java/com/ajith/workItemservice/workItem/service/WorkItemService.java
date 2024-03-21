package com.ajith.workItemservice.workItem.service;

import com.ajith.workItemservice.exceptions.ResourceNotFountException;
import com.ajith.workItemservice.feign.project.ProjectFeignService;
import com.ajith.workItemservice.utils.BasicResponse;
import com.ajith.workItemservice.workItem.entity.WorkItem;
import com.ajith.workItemservice.workItem.enums.WorkItemCategory;
import com.ajith.workItemservice.workItem.repository.WorkItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            String projectId, String authHeader) {

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

    private void createWorkItemAndSave (String workItemCategory, String title, Optional < String > parentWorkItemId, String projectId) {
        WorkItem newWorkItem = WorkItem.builder ()
                .parentWorkItemId ( parentWorkItemId.isPresent () ? Long.valueOf ( parentWorkItemId.get () ) : null )
                .title ( title )
                .category ( WorkItemCategory.valueOf ( workItemCategory ) )
                .projectId ( Long.valueOf ( projectId ) )
                .build ();
        workItemRepository.save ( newWorkItem );
    }
}
