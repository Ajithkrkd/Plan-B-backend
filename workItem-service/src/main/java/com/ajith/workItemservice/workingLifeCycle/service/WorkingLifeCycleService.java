package com.ajith.workItemservice.workingLifeCycle.service;

import com.ajith.workItemservice.exceptions.ResourceNotFountException;
import com.ajith.workItemservice.exceptions.UserNotFoundException;
import com.ajith.workItemservice.feign.member.MemberFeignService;
import com.ajith.workItemservice.members.dto.User;
import com.ajith.workItemservice.utils.BasicResponse;
import com.ajith.workItemservice.workingLifeCycle.dto.WorkingLifeCycleDto;
import com.ajith.workItemservice.workingLifeCycle.entity.WorkingLifeCycle;
import com.ajith.workItemservice.workingLifeCycle.repository.WorkingLifeCycleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkingLifeCycleService implements IWorkingLifeCycleService{

    private final MemberFeignService memberFeignService;
    private final WorkingLifeCycleRepository workingLifeCycleRepository;
    @Override
    public ResponseEntity < List < WorkingLifeCycle > > getAllWorkingLifeCycles (String authHeader) {
        try {
            User expectedUser = memberFeignService.getUserByAuthHeader ( authHeader ).getBody ();
            if(expectedUser == null) {
                throw new UserNotFoundException ( "User does not exist with auth header " + authHeader);
            }
            List<WorkingLifeCycle> workingLifeCycles =
                    workingLifeCycleRepository.findAllByCreatedByAndIsDeletedFalse(expectedUser.getUserId ());
            return ResponseEntity.ok (workingLifeCycles);
        } catch (UserNotFoundException e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity < BasicResponse > createWorkingLifeCycle (String authHeader, WorkingLifeCycleDto workingLifeCycleDto) {
        try {
            User expectedUser = memberFeignService.getUserByAuthHeader ( authHeader ).getBody ();
            if(expectedUser == null) {
                throw new UserNotFoundException ( "User does not exist with auth header " + authHeader);
            }
            WorkingLifeCycle workingLifeCycle =  mapWorkingLifecycleDtoToWorkingLifeCycle ( workingLifeCycleDto, expectedUser );
            workingLifeCycleRepository.save ( workingLifeCycle );
            return ResponseEntity.ok ( BasicResponse.builder()
                            .message ( "working life cycle created successfully" )
                            .description ("New working life cycle is created successfully you can add this to work items")
                            .status ( HttpStatus.CREATED.value ( ) )
                            .timestamp ( LocalDateTime.now () )
                    .build() );
        } catch (UserNotFoundException e) {
            throw e;
        }
    }

    @Override
    public ResponseEntity < BasicResponse > editWorkingLifeCycle (String workingLifeCycleId, WorkingLifeCycleDto workingLifeCycleDto) {
        try{
            Optional<WorkingLifeCycle> optionalWorkingLifeCycle
                    = workingLifeCycleRepository.findById ( Long.valueOf ( workingLifeCycleId ) );
            if (!optionalWorkingLifeCycle.isPresent ()){
                throw new ResourceNotFountException ("working life cycle does not exist with id "+ workingLifeCycleId);
            }
            WorkingLifeCycle existingWorkingLifeCycle = optionalWorkingLifeCycle.get ();
            if(workingLifeCycleDto.getTitle () == null || workingLifeCycleDto.getStartTime () == null || workingLifeCycleDto.getEndTime () == null){
                throw new NullPointerException ( "expected values is not present while editing working life cycle" +workingLifeCycleDto.toString () );
            }
            existingWorkingLifeCycle.setTitle ( workingLifeCycleDto.getTitle () );
            existingWorkingLifeCycle.setStartTime ( workingLifeCycleDto.getStartTime ());
            existingWorkingLifeCycle.setEndTime ( workingLifeCycleDto.getEndTime() );
            workingLifeCycleRepository.save (existingWorkingLifeCycle );

            return ResponseEntity.ok ( BasicResponse.builder()
                            .message ( "working life cycle is edited successfully" )
                            .description ( "working life cycle edited with id "+ workingLifeCycleId )
                            .timestamp ( LocalDateTime.now (  ) )
                            .status ( HttpStatus.OK.value ( ) )
                    .build() );
        } catch (NumberFormatException e) {
            throw new RuntimeException ( e );
        } catch (ResourceNotFountException e) {
            throw new RuntimeException ( e );
        } catch (NullPointerException e) {
            throw new RuntimeException ( e );
        }
    }

    @Override
    public ResponseEntity < BasicResponse > deleteWorkingLifeCycle (String workingLifeCycleId) {
        try{
            Optional<WorkingLifeCycle> optionalWorkingLifeCycle
                    = workingLifeCycleRepository.findById ( Long.valueOf ( workingLifeCycleId ) );
            if (!optionalWorkingLifeCycle.isPresent ()){
                throw new ResourceNotFountException ("working life cycle does not exist with id "+ workingLifeCycleId);
            }
            WorkingLifeCycle existingWorkingLifeCycle = optionalWorkingLifeCycle.get ();
            existingWorkingLifeCycle.setDeleted ( true );
            workingLifeCycleRepository.save ( existingWorkingLifeCycle );

            return ResponseEntity.ok ( BasicResponse.builder()
                    .message ( "working life cycle is deleted successfully" )
                    .description ( "working life cycle deleted with id "+ workingLifeCycleId )
                    .timestamp ( LocalDateTime.now (  ) )
                    .status ( HttpStatus.OK.value ( ) )
                    .build() );
        } catch (NumberFormatException e) {
            throw new RuntimeException ( e );
        } catch (ResourceNotFountException e) {
            throw new RuntimeException ( e );
        }
    }

    private static   WorkingLifeCycle mapWorkingLifecycleDtoToWorkingLifeCycle (WorkingLifeCycleDto workingLifeCycleDto, User expectedUser) {
       return WorkingLifeCycle.builder ( )
                .createdBy ( expectedUser.getUserId ( ) )
                .title ( workingLifeCycleDto.getTitle ( ) )
                .startTime ( workingLifeCycleDto.getStartTime () )
                .endTime ( workingLifeCycleDto.getEndTime () )
                .build ( );
    }
}
