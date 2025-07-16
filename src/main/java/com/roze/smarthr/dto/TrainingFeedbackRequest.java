
package com.roze.smarthr.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingFeedbackRequest {
    @NotNull(message = "Employee training ID is required")
    private Long employeeTrainingId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    private String comments;
    private boolean trainerFeedback;
}