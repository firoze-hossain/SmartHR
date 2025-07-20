package com.roze.smarthr.service;

import com.roze.smarthr.dto.OnboardingTaskRequest;
import com.roze.smarthr.dto.OnboardingTaskResponse;

import java.util.List;

public interface OnboardingService {
    OnboardingTaskResponse createOnboardingTask(OnboardingTaskRequest request);

    OnboardingTaskResponse getOnboardingTaskById(Long id);

    List<OnboardingTaskResponse> getOnboardingTasksByCandidate(Long candidateId);

    OnboardingTaskResponse updateOnboardingTaskStatus(Long id, String status, String comments);

    void generateDefaultOnboardingTasks(Long candidateId);

    void sendOnboardingTaskReminders();
}