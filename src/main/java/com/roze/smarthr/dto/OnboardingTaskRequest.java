package com.roze.smarthr.dto;

import com.roze.smarthr.enums.OnboardingTaskStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OnboardingTaskRequest {
    @NotNull(message = "Candidate ID cannot be null")
    private Long candidateId;

    @NotBlank(message = "Task name cannot be blank")
    private String taskName;

    private String description;

    @NotNull(message = "Assigned to ID cannot be null")
    private Long assignedToId;

    @FutureOrPresent(message = "Deadline must be in the present or future")
    @NotNull(message = "Deadline cannot be null")
    private LocalDate deadline;
}

