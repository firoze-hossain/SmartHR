package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.JobPostRequest;
import com.roze.smarthr.dto.JobPostResponse;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.service.JobPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("job_posts")
@RequiredArgsConstructor
public class JobPostController {
    private final JobPostService jobPostService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping
    public ResponseEntity<BaseResponse<JobPostResponse>> createJobPost(
            @Valid @RequestBody JobPostRequest request,
            @AuthenticationPrincipal User currentUser) {
        JobPostResponse response = jobPostService.createJobPost(request, currentUser);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<JobPostResponse>> getJobPostById(@PathVariable Long id) {
        JobPostResponse response = jobPostService.getJobPostById(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<JobPostResponse>>> getAllJobPosts() {
        List<JobPostResponse> responses = jobPostService.getAllJobPosts();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/active")
    public ResponseEntity<BaseResponse<List<JobPostResponse>>> getActiveJobPosts() {
        List<JobPostResponse> responses = jobPostService.getActiveJobPosts();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<JobPostResponse>> updateJobPost(
            @PathVariable Long id,
            @Valid @RequestBody JobPostRequest request) {
        JobPostResponse response = jobPostService.updateJobPost(id, request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.UPDATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping("/{id}/close")
    public ResponseEntity<BaseResponse<Void>> closeJobPost(@PathVariable Long id) {
        jobPostService.closeJobPost(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Job post closed successfully",
                null
        ));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<BaseResponse<List<JobPostResponse>>> getJobPostsByDepartment(
            @PathVariable Long departmentId) {
        List<JobPostResponse> responses = jobPostService.getJobPostsByDepartment(departmentId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }
}