package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.OnboardingTaskRequest;
import com.roze.smarthr.dto.OnboardingTaskResponse;
import com.roze.smarthr.entity.Candidate;
import com.roze.smarthr.entity.OnboardingTask;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.CandidateRepository;
import com.roze.smarthr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnboardingTaskMapper {
    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;

    public OnboardingTask toEntity(OnboardingTaskRequest request) {
        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + request.getCandidateId()));

        User assignedTo = userRepository.findById(request.getAssignedToId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAssignedToId()));

        return OnboardingTask.builder()
                .candidate(candidate)
                .taskName(request.getTaskName())
                .description(request.getDescription())
                .assignedTo(assignedTo)
                .deadline(request.getDeadline())
                .build();
    }

    public OnboardingTaskResponse toDto(OnboardingTask task) {
        return OnboardingTaskResponse.builder()
                .id(task.getId())
                .candidateId(task.getCandidate().getId())
                .candidateName(task.getCandidate().getFullName())
                .taskName(task.getTaskName())
                .description(task.getDescription())
                .assignedToId(task.getAssignedTo().getId())
                .assignedToName(task.getAssignedTo().getUsername())
                .deadline(task.getDeadline())
                .status(task.getStatus())
                .comments(task.getComments())
                .build();
    }
}