package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.InterviewRequest;
import com.roze.smarthr.dto.InterviewResponse;
import com.roze.smarthr.entity.Candidate;
import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.Interview;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.CandidateRepository;
import com.roze.smarthr.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterviewMapper {
    private final CandidateRepository candidateRepository;
    private final EmployeeRepository employeeRepository;

    public Interview toEntity(InterviewRequest request) {
        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + request.getCandidateId()));

        Employee interviewer = employeeRepository.findById(request.getInterviewerId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getInterviewerId()));

        return Interview.builder()
                .candidate(candidate)
                .interviewer(interviewer)
                .interviewType(request.getInterviewType())
                .scheduledDate(request.getScheduledDate())
                .meetingLink(request.getMeetingLink())
                .build();
    }

    public InterviewResponse toDto(Interview interview) {
        return InterviewResponse.builder()
                .id(interview.getId())
                .candidateId(interview.getCandidate().getId())
                .candidateName(interview.getCandidate().getFullName())
                .interviewerId(interview.getInterviewer().getId())
                .interviewerName(interview.getInterviewer().getName())
                .interviewType(interview.getInterviewType())
                .scheduledDate(interview.getScheduledDate())
                .feedback(interview.getFeedback())
                .result(interview.getResult())
                .meetingLink(interview.getMeetingLink())
                .build();
    }
}