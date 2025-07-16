
package com.roze.smarthr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingFeedbackResponse {
    private Long id;
    private Long employeeTrainingId;
    private int rating;
    private String comments;
    private LocalDate submittedDate;
    private boolean trainerFeedback;
    private String employeeName;
    private String trainingProgramTitle;
}