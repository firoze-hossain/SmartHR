
package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.*;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("trainings")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping("/programs")
    public ResponseEntity<BaseResponse<TrainingProgramResponse>> createTrainingProgram(
            @Valid @RequestBody TrainingProgramRequest request) {
        TrainingProgramResponse response = trainingService.createTrainingProgram(request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PutMapping("/programs/{id}")
    public ResponseEntity<BaseResponse<TrainingProgramResponse>> updateTrainingProgram(
            @PathVariable Long id,
            @Valid @RequestBody TrainingProgramRequest request) {
        TrainingProgramResponse response = trainingService.updateTrainingProgram(id, request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.UPDATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @DeleteMapping("/programs/{id}")
    public ResponseEntity<BaseResponse<Void>> deactivateTrainingProgram(@PathVariable Long id) {
        trainingService.deactivateTrainingProgram(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.DELETE_SUCCESS,
                null
        ));
    }

    @GetMapping("/programs/{id}")
    public ResponseEntity<BaseResponse<TrainingProgramResponse>> getTrainingProgramById(@PathVariable Long id) {
        TrainingProgramResponse response = trainingService.getTrainingProgramById(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @GetMapping("/programs")
    public ResponseEntity<BaseResponse<List<TrainingProgramResponse>>> getAllTrainingPrograms(
            @RequestParam(required = false, defaultValue = "true") boolean activeOnly) {
        List<TrainingProgramResponse> responses = trainingService.getAllTrainingPrograms(activeOnly);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/programs/type/{type}")
    public ResponseEntity<BaseResponse<List<TrainingProgramResponse>>> getTrainingProgramsByType(
            @PathVariable String type) {
        List<TrainingProgramResponse> responses = trainingService.getTrainingProgramsByType(type);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/departments/{departmentId}/programs")
    public ResponseEntity<BaseResponse<List<TrainingProgramResponse>>> getTrainingProgramsByDepartment(
            @PathVariable Long departmentId) {
        List<TrainingProgramResponse> responses = trainingService.getTrainingProgramsByDepartment(departmentId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/departments/{departmentId}/upcoming")
    public ResponseEntity<BaseResponse<List<TrainingProgramResponse>>> getUpcomingDepartmentTrainings(
            @PathVariable Long departmentId) {
        List<TrainingProgramResponse> responses = trainingService.getUpcomingDepartmentTrainings(departmentId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping("/enrollments")
    public ResponseEntity<BaseResponse<EmployeeTrainingResponse>> enrollEmployee(
            @Valid @RequestBody EmployeeTrainingRequest request) {
        EmployeeTrainingResponse response = trainingService.enrollEmployee(request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Employee enrolled successfully",
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PutMapping("/enrollments/{id}")
    public ResponseEntity<BaseResponse<EmployeeTrainingResponse>> updateEmployeeTrainingStatus(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeTrainingRequest request) {
        EmployeeTrainingResponse response = trainingService.updateEmployeeTrainingStatus(id, request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Training status updated successfully",
                response
        ));
    }

    @GetMapping("/employees/{employeeId}/trainings")
    public ResponseEntity<BaseResponse<List<EmployeeTrainingResponse>>> getEmployeeTrainings(
            @PathVariable Long employeeId) {
        List<EmployeeTrainingResponse> responses = trainingService.getEmployeeTrainings(employeeId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/programs/{programId}/participants")
    public ResponseEntity<BaseResponse<List<EmployeeTrainingResponse>>> getTrainingProgramParticipants(
            @PathVariable Long programId) {
        List<EmployeeTrainingResponse> responses = trainingService.getTrainingProgramParticipants(programId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping("/feedback")
    public ResponseEntity<BaseResponse<TrainingFeedbackResponse>> submitFeedback(
            @Valid @RequestBody TrainingFeedbackRequest request,
            @AuthenticationPrincipal User user) {
        TrainingFeedbackResponse response = trainingService.submitFeedback(request, user);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Feedback submitted successfully",
                response
        ));
    }

    @GetMapping("/programs/{programId}/feedback")
    public ResponseEntity<BaseResponse<List<TrainingFeedbackResponse>>> getFeedbackForTraining(
            @PathVariable Long programId) {
        List<TrainingFeedbackResponse> responses = trainingService.getFeedbackForTraining(programId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/trainers/{trainerName}/feedback")
    public ResponseEntity<BaseResponse<List<TrainingFeedbackResponse>>> getFeedbackForTrainer(
            @PathVariable String trainerName) {
        List<TrainingFeedbackResponse> responses = trainingService.getFeedbackForTrainer(trainerName);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping("/employees/{employeeId}/mandatory")
    public ResponseEntity<BaseResponse<List<EmployeeTrainingResponse>>> getIncompleteMandatoryTrainings(
            @PathVariable Long employeeId) {
        List<EmployeeTrainingResponse> responses = trainingService.getIncompleteMandatoryTrainings(employeeId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }
}