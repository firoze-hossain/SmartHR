package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.OnboardingTaskRequest;
import com.roze.smarthr.dto.OnboardingTaskResponse;
import com.roze.smarthr.entity.Candidate;
import com.roze.smarthr.entity.OnboardingTask;
import com.roze.smarthr.enums.OnboardingTaskStatus;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.OnboardingTaskMapper;
import com.roze.smarthr.repository.CandidateRepository;
import com.roze.smarthr.repository.OnboardingTaskRepository;
import com.roze.smarthr.service.NotificationService;
import com.roze.smarthr.service.OnboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnboardingServiceImpl implements OnboardingService {
    private final OnboardingTaskRepository onboardingTaskRepository;
    private final OnboardingTaskMapper onboardingTaskMapper;
    private final CandidateRepository candidateRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public OnboardingTaskResponse createOnboardingTask(OnboardingTaskRequest request) {
        OnboardingTask task = onboardingTaskMapper.toEntity(request);
        OnboardingTask savedTask = onboardingTaskRepository.save(task);

        // Send notification to assigned user
        notificationService.sendOnboardingTaskAssignmentNotification(
                savedTask.getAssignedTo().getEmail(),
                savedTask.getTaskName(),
                savedTask.getDeadline());

        return onboardingTaskMapper.toDto(savedTask);
    }

    @Override
    public OnboardingTaskResponse getOnboardingTaskById(Long id) {
        OnboardingTask task = onboardingTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Onboarding task not found with id: " + id));
        return onboardingTaskMapper.toDto(task);
    }

    @Override
    public List<OnboardingTaskResponse> getOnboardingTasksByCandidate(Long candidateId) {
        return onboardingTaskRepository.findByCandidateId(candidateId).stream()
                .map(onboardingTaskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OnboardingTaskResponse updateOnboardingTaskStatus(Long id, String status, String comments) {
        OnboardingTask task = onboardingTaskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Onboarding task not found with id: " + id));

        try {
            OnboardingTaskStatus newStatus = OnboardingTaskStatus.valueOf(status.toUpperCase());
            task.setStatus(newStatus);
            task.setComments(comments);
            OnboardingTask updatedTask = onboardingTaskRepository.save(task);

            // Notify HR about task completion
            if (newStatus == OnboardingTaskStatus.DONE) {
                notificationService.sendOnboardingTaskCompletionNotification(
                        task.getCandidate().getEmail(),
                        task.getTaskName());
            }

            return onboardingTaskMapper.toDto(updatedTask);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    @Override
    @Transactional
    public void generateDefaultOnboardingTasks(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));

        // Default onboarding tasks
        List<String> defaultTasks = Arrays.asList(
                "Document Verification",
                "Account Setup",
                "Equipment Provisioning",
                "Orientation Session",
                "Policy Acknowledgment"
        );

        LocalDate deadline = LocalDate.now().plusDays(7); // Default deadline 7 days from now

        for (String taskName : defaultTasks) {
            OnboardingTask task = OnboardingTask.builder()
                    .candidate(candidate)
                    .taskName(taskName)
                    .description("Complete " + taskName + " for new hire onboarding")
                    .assignedTo(candidate.getJobPost().getPostedBy()) // Assigned to the HR who posted the job
                    .deadline(deadline)
                    .build();

            onboardingTaskRepository.save(task);
        }
    }

    @Override
    public void sendOnboardingTaskReminders() {
        LocalDate today = LocalDate.now();
        List<OnboardingTask> overdueTasks = onboardingTaskRepository
                .findByStatusAndDeadlineBefore(OnboardingTaskStatus.PENDING, today);

        for (OnboardingTask task : overdueTasks) {
            notificationService.sendOnboardingTaskReminder(
                    task.getAssignedTo().getEmail(),
                    task.getTaskName(),
                    task.getDeadline());
        }
    }
}