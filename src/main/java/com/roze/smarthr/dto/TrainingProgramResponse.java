package com.roze.smarthr.dto;

import com.roze.smarthr.enums.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingProgramResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private String trainerName;
    private boolean mandatory;
    private TrainingType type;
    private DepartmentResponse department;
    private Integer maxParticipants;
    private boolean active;
    private Integer enrolledCount;
    private Integer completedCount;
    private LocalDate createdAt;
}