package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.CandidateRequest;
import com.roze.smarthr.dto.CandidateResponse;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.service.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("candidates")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<CandidateResponse>> createCandidate(
            @Valid @RequestBody CandidateRequest request) {
        CandidateResponse response = candidateService.createCandidate(request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CandidateResponse>> getCandidateById(@PathVariable Long id) {
        CandidateResponse response = candidateService.getCandidateById(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<CandidateResponse>>> getAllCandidates() {
        List<CandidateResponse> responses = candidateService.getAllCandidates();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/job_post/{jobPostId}")
    public ResponseEntity<BaseResponse<List<CandidateResponse>>> getCandidatesByJobPost(
            @PathVariable Long jobPostId) {
        List<CandidateResponse> responses = candidateService.getCandidatesByJobPost(jobPostId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<BaseResponse<CandidateResponse>> updateCandidateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        CandidateResponse response = candidateService.updateCandidateStatus(id, status);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Candidate status updated successfully",
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping("/{id}/create_account")
    public ResponseEntity<BaseResponse<CandidateResponse>> createCandidateUserAccount(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        CandidateResponse response = candidateService.createCandidateUserAccount(id, currentUser);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Candidate user account created successfully",
                response
        ));
    }
}