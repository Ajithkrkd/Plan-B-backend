package com.ajith.workItemservice.label.service;

import com.ajith.workItemservice.exceptions.ResourceDuplicationException;
import com.ajith.workItemservice.exceptions.ResourceNotFountException;
import com.ajith.workItemservice.label.entity.Label;
import com.ajith.workItemservice.label.repository.LabelRepository;
import com.ajith.workItemservice.label.service.ILabelService;
import com.ajith.workItemservice.members.dto.User;
import com.ajith.workItemservice.utils.BasicResponse;
import com.ajith.workItemservice.workItem.entity.WorkItem;
import com.ajith.workItemservice.workItem.repository.WorkItemRepository;
import com.ajith.workItemservice.workItem.service.IWorkItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LabelService implements ILabelService {

    private final WorkItemRepository workItemRepository;
    private final LabelRepository labelRepository;
    @Override
    public ResponseEntity < BasicResponse > createAndAddLabelToWorkItem (
            String workItemId, String labelTitle, User expectedUser) {
        try{
            Optional< WorkItem > optionalWorkItem = workItemRepository.findById ( Long.valueOf ( workItemId ) );
            if(!optionalWorkItem.isPresent ()){
                throw new ResourceNotFountException ( "Work item does not exist with this work item id "+ workItemId );
            }
            //TODO: add colors for the labels
            WorkItem existingWorkItem = optionalWorkItem.get();
            if( existingWorkItem.getLabels ().stream ().map ( label -> label.getLabelName () == labelTitle ).findFirst ().isPresent ()){
                log.info ( "Work item with label "+ labelTitle+" already exists" );
                throw new ResourceDuplicationException ( "Work item with label "+ labelTitle+" already exists");
            }
            Label newLabel = createAndSaveLabel ( labelTitle, expectedUser, existingWorkItem );
            existingWorkItem.getLabels ().add ( newLabel );
            workItemRepository.save ( existingWorkItem );
            return ResponseEntity.ok ( BasicResponse.builder ( )
                    .description ( "Work item with label " + labelTitle + " created successfully" )
                    .message ( "label created successfully" )
                    .status ( HttpStatus.CREATED.value ( ) )
                    .timestamp ( LocalDateTime.now ( ) )
                    .build ( ) );

        } catch (ResourceNotFountException e) {
            throw new RuntimeException ( e );
        } catch (ResourceDuplicationException e) {
            throw new RuntimeException ( e );
        }
    }

    @Override
    public ResponseEntity<BasicResponse> deleteLabel(String labelId, String workItemId) {
        try {
            Long workId = Long.valueOf(workItemId);
            Optional<WorkItem> optionalWorkItem = workItemRepository.findById(workId);
            if (!optionalWorkItem.isPresent()) {
                throw new ResourceNotFountException ("Work item does not exist with this work item id " + workItemId);
            }
            WorkItem existingWorkItem = optionalWorkItem.get();
            List<Label> labelList = existingWorkItem.getLabels();

            if (labelList.isEmpty()) {
                throw new ResourceNotFountException ("No labels associated with work item " + workItemId);
            }
            Optional<Label> labelToRemove = labelList.stream()
                    .filter(label -> label.getLabelId().equals(Long.valueOf(labelId)))
                    .findFirst();

            if (!labelToRemove.isPresent()) {
                throw new ResourceNotFountException ("Label with id " + labelId + " is not associated with work item " + workItemId);
            }
            labelList.remove(labelToRemove.get());
            workItemRepository.save(existingWorkItem);

            return ResponseEntity.ok().body(new BasicResponse(
                    HttpStatus.OK.value (),
                    "label deleted successfully" ,
                    "Label with id " + labelId + " is not associated with work item " + workItemId,
                    LocalDateTime.now ()));

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid work item id or label id provided");
        } catch (ResourceNotFountException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while deleting label from work item", e);
        }
    }

    @Override
    public ResponseEntity < List < String > > getAllLabelsByWorkItemId (String workItemId) {

        Long workId = Long.valueOf(workItemId);
        Optional<WorkItem> optionalWorkItem = workItemRepository.findById(workId);
        if (!optionalWorkItem.isPresent()) {
            throw new ResourceNotFountException ("Work item does not exist with this work item id " + workItemId);
        }
        WorkItem existingWorkItem = optionalWorkItem.get();

        List<Label> labelList = labelRepository.findAllByWorkItemId(workId);
        if (labelList.isEmpty ()){
            return ResponseEntity.ok ( Collections.emptyList () );
        }
        List<String> labelNames = labelList.stream()
                .map(Label::getLabelName)
                .collect( Collectors.toList());
        return ResponseEntity.ok().body(labelNames);
    }


    private Label createAndSaveLabel (String labelTitle, User expectedUser, WorkItem existingWorkItem) {
        Label newLabel = Label.builder ( )
                .createdBy ( expectedUser.getUserId ( ) )
                .labelName ( labelTitle )
                .workItemId ( existingWorkItem.getWorkItemId ( ) )
                .build ( );
        return labelRepository.save ( newLabel );
    }



}
