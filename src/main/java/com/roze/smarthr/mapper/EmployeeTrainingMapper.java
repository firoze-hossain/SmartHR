
package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.EmployeeTrainingRequest;
import com.roze.smarthr.dto.EmployeeTrainingResponse;
import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.EmployeeTraining;
import com.roze.smarthr.entity.TrainingProgram;
import com.roze.smarthr.enums.TrainingStatus;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.EmployeeRepository;
import com.roze.smarthr.repository.TrainingProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class EmployeeTrainingMapper {
    private final EmployeeRepository employeeRepository;
    private final TrainingProgramRepository trainingProgramRepository;

    public EmployeeTraining toEntity(EmployeeTrainingRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        TrainingProgram trainingProgram = trainingProgramRepository.findById(request.getTrainingProgramId())
                .orElseThrow(() -> new ResourceNotFoundException("Training program not found"));

        return EmployeeTraining.builder()
                .employee(employee)
                .trainingProgram(trainingProgram)
                .status(request.getStatus() != null ? request.getStatus() : TrainingStatus.ENROLLED)
                .enrolledDate(LocalDate.now())
                .completionDate(request.getCompletionDate())
                .score(request.getScore())
                .certificateId(request.getCertificateId())
                .build();
    }

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