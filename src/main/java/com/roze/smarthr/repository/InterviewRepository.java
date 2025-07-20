package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Interview;
import com.roze.smarthr.entity.Candidate;
import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.enums.InterviewResult;
import com.roze.smarthr.enums.InterviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long>, JpaSpecificationExecutor<Interview> {
    List<Interview> findByCandidate(Candidate candidate);

    List<Interview> findByInterviewer(Employee interviewer);

    List<Interview> findByInterviewTypeAndResult(InterviewType interviewType, InterviewResult result);

    List<Interview> findByScheduledDateBetween(LocalDateTime start, LocalDateTime end);

    List<Interview> findByCandidateId(Long candidateId);

    List<Interview> findByInterviewerIdAndScheduledDateBetween(Long id, LocalDateTime now, LocalDateTime endOfDay);
}