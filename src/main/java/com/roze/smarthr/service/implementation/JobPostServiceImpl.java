package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.JobPostRequest;
import com.roze.smarthr.dto.JobPostResponse;
import com.roze.smarthr.entity.JobPost;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.enums.JobPostStatus;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.JobPostMapper;
import com.roze.smarthr.repository.JobPostRepository;
import com.roze.smarthr.service.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {
    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;

    @Override
    @Transactional
    public JobPostResponse createJobPost(JobPostRequest request, User currentUser) {
        JobPost jobPost = jobPostMapper.toEntity(request, currentUser);
        JobPost savedJobPost = jobPostRepository.save(jobPost);
        return jobPostMapper.toDto(savedJobPost);
    }

    @Override
    public JobPostResponse getJobPostById(Long id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job post not found with id: " + id));
        return jobPostMapper.toDto(jobPost);
    }

    @Override
    public List<JobPostResponse> getAllJobPosts() {
        return jobPostRepository.findAll().stream()
                .map(jobPostMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostResponse> getActiveJobPosts() {
        return jobPostRepository.findActiveJobPosts(LocalDate.now()).stream()
                .map(jobPostMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobPostResponse updateJobPost(Long id, JobPostRequest request) {
        JobPost existingJobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job post not found with id: " + id));

        existingJobPost.setTitle(request.getTitle());
        existingJobPost.setLocation(request.getLocation());
        existingJobPost.setEmploymentType(request.getEmploymentType());
        existingJobPost.setJobDescription(request.getJobDescription());
        existingJobPost.setRequirements(request.getRequirements());
        existingJobPost.setClosingDate(request.getClosingDate());

        JobPost updatedJobPost = jobPostRepository.save(existingJobPost);
        return jobPostMapper.toDto(updatedJobPost);
    }

    @Override
    @Transactional
    public void closeJobPost(Long id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job post not found with id: " + id));
        jobPost.setStatus(JobPostStatus.CLOSED);
        jobPostRepository.save(jobPost);
    }

    @Override
    public List<JobPostResponse> getJobPostsByDepartment(Long departmentId) {
        return jobPostRepository.findByDepartmentId(departmentId).stream()
                .map(jobPostMapper::toDto)
                .collect(Collectors.toList());
    }
}