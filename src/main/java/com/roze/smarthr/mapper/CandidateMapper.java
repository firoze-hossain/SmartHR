package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.CandidateRequest;
import com.roze.smarthr.dto.CandidateResponse;
import com.roze.smarthr.entity.Candidate;
import com.roze.smarthr.entity.JobPost;
import com.roze.smarthr.enums.CandidateStatus;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.JobPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CandidateMapper {
    private final JobPostRepository jobPostRepository;

    public Candidate toEntity(CandidateRequest request) {
        JobPost jobPost = jobPostRepository.findById(request.getJobPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Job post not found with id: " + request.getJobPostId()));

        return Candidate.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .resumeUrl(request.getResumeUrl())
                .appliedDate(LocalDate.now())
                .jobPost(jobPost)
                .status(CandidateStatus.APPLIED)
                .build();
    }

    public CandidateResponse toDto(Candidate candidate) {
        return CandidateResponse.builder()
                .id(candidate.getId())
                .fullName(candidate.getFullName())
                .email(candidate.getEmail())
                .phone(candidate.getPhone())
                .resumeUrl(candidate.getResumeUrl())
                .appliedDate(candidate.getAppliedDate())
                .jobPostId(candidate.getJobPost().getId())
                .jobPostTitle(candidate.getJobPost().getTitle())
                .status(candidate.getStatus())
                .userId(candidate.getUser() != null ? candidate.getUser().getId() : null)
                .offerLetterId(candidate.getOfferLetter() != null ? candidate.getOfferLetter().getId() : null)
                .build();
    }
}