
package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.EmployeeTrainingResponse;
import com.roze.smarthr.entity.EmployeeTraining;
import org.springframework.stereotype.Component;

@Component
public class EmployeeTrainingMapper {
    public EmployeeTrainingResponse toEmployeeTrainingResponse(EmployeeTraining employeeTraining) {
        return EmployeeTrainingResponse.builder()
                .id(employeeTraining.getId())
                .employeeId(employeeTraining.getEmployee().getId())
                .employeeName(employeeTraining.getEmployee().getName())
                .trainingProgramId(employeeTraining.getTrainingProgram().getId())
                .trainingProgramTitle(employeeTraining.getTrainingProgram().getTitle())
                .status(employeeTraining.getStatus())
                .enrolledDate(employeeTraining.getEnrolledDate())
                .completionDate(employeeTraining.getCompletionDate())
                .score(employeeTraining.getScore())
                .certificateId(employeeTraining.getCertificateId())
                .feedbackSubmitted(employeeTraining.isFeedbackSubmitted())
                .build();
    }
}