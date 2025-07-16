
package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.TrainingFeedbackResponse;
import com.roze.smarthr.entity.TrainingFeedback;
import org.springframework.stereotype.Component;

@Component
public class TrainingFeedbackMapper {
    public TrainingFeedbackResponse toTrainingFeedbackResponse(TrainingFeedback feedback) {
        return TrainingFeedbackResponse.builder()
                .id(feedback.getId())
                .employeeTrainingId(feedback.getEmployeeTraining().getId())
                .rating(feedback.getRating())
                .comments(feedback.getComments())
                .submittedDate(feedback.getSubmittedDate())
                .trainerFeedback(feedback.isTrainerFeedback())
                .employeeName(feedback.getEmployeeTraining().getEmployee().getName())
                .trainingProgramTitle(feedback.getEmployeeTraining().getTrainingProgram().getTitle())
                .build();
    }
}