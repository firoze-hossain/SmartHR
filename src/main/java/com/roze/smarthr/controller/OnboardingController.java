package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.OnboardingTaskRequest;
import com.roze.smarthr.dto.OnboardingTaskResponse;
import com.roze.smarthr.service.OnboardingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/onboarding_tasks")
@RequiredArgsConstructor
public class OnboardingController {
    private final OnboardingService onboardingService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping
    public ResponseEntity<BaseResponse<OnboardingTaskResponse>> createOnboardingTask(
            @Valid @RequestBody OnboardingTaskRequest request) {
        OnboardingTaskResponse response = onboardingService.createOnboardingTask(request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OnboardingTaskResponse>> getOnboardingTaskById(@PathVariable Long id) {
        OnboardingTaskResponse response = onboardingService.getOnboardingTaskById(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<BaseResponse<List<OnboardingTaskResponse>>> getOnboardingTasksByCandidate(
            @PathVariable Long candidateId) {
        List<OnboardingTaskResponse> responses = onboardingService.getOnboardingTasksByCandidate(candidateId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR', 'ONBOARDING_ASSIGNEE')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<BaseResponse<OnboardingTaskResponse>> updateOnboardingTaskStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String comments) {
        OnboardingTaskResponse response = onboardingService.updateOnboardingTaskStatus(id, status, comments);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Onboarding task status updated successfully",
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping("/{candidateId}/generate-default-tasks")
    public ResponseEntity<BaseResponse<Void>> generateDefaultOnboardingTasks(@PathVariable Long candidateId) {
        onboardingService.generateDefaultOnboardingTasks(candidateId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Default onboarding tasks generated successfully",
                null
        ));
    }
}