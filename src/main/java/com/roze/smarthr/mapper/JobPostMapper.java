package com.roze.smarthr.mapper;


import com.roze.smarthr.dto.JobPostRequest;
import com.roze.smarthr.dto.JobPostResponse;
import com.roze.smarthr.entity.Department;
import com.roze.smarthr.entity.JobPost;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.enums.JobPostStatus;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class JobPostMapper {
    private final DepartmentRepository departmentRepository;

    public JobPost toEntity(JobPostRequest request, User postedBy) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));

        return JobPost.builder()
                .title(request.getTitle())
                .department(department)
                .location(request.getLocation())
                .employmentType(request.getEmploymentType())
                .jobDescription(request.getJobDescription())
                .requirements(request.getRequirements())
                .status(JobPostStatus.OPEN)
                .postedDate(LocalDate.now())
                .closingDate(request.getClosingDate())
                .postedBy(postedBy)
                .build();
    }

    public JobPostResponse toDto(JobPost jobPost) {
        return JobPostResponse.builder()
                .id(jobPost.getId())
                .title(jobPost.getTitle())
                .departmentId(jobPost.getDepartment().getId())
                .departmentName(jobPost.getDepartment().getTitle())
                .location(jobPost.getLocation())
                .employmentType(jobPost.getEmploymentType())
                .jobDescription(jobPost.getJobDescription())
                .requirements(jobPost.getRequirements())
                .status(jobPost.getStatus())
                .postedDate(jobPost.getPostedDate())
                .closingDate(jobPost.getClosingDate())
                .postedById(jobPost.getPostedBy().getId())
                .postedByName(jobPost.getPostedBy().getUsername())
                .build();
    }
}