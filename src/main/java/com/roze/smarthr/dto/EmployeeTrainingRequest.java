
package com.roze.smarthr.dto;

import com.roze.smarthr.enums.TrainingStatus;
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
public class EmployeeTrainingRequest {
    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Training program ID is required")
    private Long trainingProgramId;

    private TrainingStatus status;
    private LocalDate completionDate;
    private Integer score;
    private String certificateId;
}