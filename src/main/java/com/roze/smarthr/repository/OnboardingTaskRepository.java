package com.roze.smarthr.repository;

import com.roze.smarthr.entity.OnboardingTask;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.enums.OnboardingTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface OnboardingTaskRepository extends JpaRepository<OnboardingTask, Long>, JpaSpecificationExecutor<OnboardingTask> {
    List<OnboardingTask> findByCandidateId(Long candidateId);

    List<OnboardingTask> findByAssignedTo(User assignedTo);

    List<OnboardingTask> findByStatusAndDeadlineBefore(OnboardingTaskStatus status, LocalDate deadline);

    List<OnboardingTask> findByCandidateIdAndStatus(Long candidateId, OnboardingTaskStatus status);
}