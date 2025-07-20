package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.InterviewRequest;
import com.roze.smarthr.dto.InterviewResponse;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("interviews")
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService interviewService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping
    public ResponseEntity<BaseResponse<InterviewResponse>> scheduleInterview(
            @Valid @RequestBody InterviewRequest request) {
        InterviewResponse response = interviewService.scheduleInterview(request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<InterviewResponse>> getInterviewById(@PathVariable Long id) {
        InterviewResponse response = interviewService.getInterviewById(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<BaseResponse<List<InterviewResponse>>> getInterviewsByCandidate(
            @PathVariable Long candidateId) {
        List<InterviewResponse> responses = interviewService.getInterviewsByCandidate(candidateId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR', 'INTERVIEWER')")
    @GetMapping("/upcoming")
    public ResponseEntity<BaseResponse<List<InterviewResponse>>> getUpcomingInterviews(
            @AuthenticationPrincipal User currentUser) {
        List<InterviewResponse> responses = interviewService.getUpcomingInterviews(currentUser);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR', 'INTERVIEWER')")
    @PostMapping("/{id}/feedback")
    public ResponseEntity<BaseResponse<InterviewResponse>> submitInterviewFeedback(
            @PathVariable Long id,
            @RequestParam String feedback,
            @RequestParam String result) {
        InterviewResponse response = interviewService.submitInterviewFeedback(id, feedback, result);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Interview feedback submitted successfully",
                response
        ));
    }
}