package com.roze.smarthr.service;

import com.roze.smarthr.dto.JobPostRequest;
import com.roze.smarthr.dto.JobPostResponse;
import com.roze.smarthr.entity.User;

import java.util.List;

public interface JobPostService {
    JobPostResponse createJobPost(JobPostRequest request, User currentUser);

    JobPostResponse getJobPostById(Long id);

    List<JobPostResponse> getAllJobPosts();

    List<JobPostResponse> getActiveJobPosts();

    JobPostResponse updateJobPost(Long id, JobPostRequest request);

    void closeJobPost(Long id);

    List<JobPostResponse> getJobPostsByDepartment(Long departmentId);
}