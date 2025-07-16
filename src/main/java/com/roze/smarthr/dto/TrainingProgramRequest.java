package com.roze.smarthr.dto;

import com.roze.smarthr.enums.TrainingType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingProgramRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Trainer name is required")
    private String trainerName;

    private boolean mandatory;

    @NotNull(message = "Training type is required")
    private TrainingType type;

    private Long departmentId;

    @Min(value = 1, message = "Max participants must be at least 1")
    private Integer maxParticipants;
}