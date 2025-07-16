
package com.roze.smarthr.dto;

import com.roze.smarthr.enums.TrainingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeTrainingResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private Long trainingProgramId;
    private String trainingProgramTitle;
    private TrainingStatus status;
    private LocalDate enrolledDate;
    private LocalDate completionDate;
    private Integer score;
    private String certificateId;
    private boolean feedbackSubmitted;
}