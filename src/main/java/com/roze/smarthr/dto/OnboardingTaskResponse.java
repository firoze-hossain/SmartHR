package com.roze.smarthr.dto;

import com.roze.smarthr.enums.OnboardingTaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnboardingTaskResponse {
    private Long id;
    private Long candidateId;
    private String candidateName;
    private String taskName;
    private String description;
    private Long assignedToId;
    private String assignedToName;
    private LocalDate deadline;
    private OnboardingTaskStatus status;
    private String comments;
}