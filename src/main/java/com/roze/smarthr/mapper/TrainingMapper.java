
package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.DepartmentResponse;
import com.roze.smarthr.dto.TrainingProgramRequest;
import com.roze.smarthr.dto.TrainingProgramResponse;
import com.roze.smarthr.entity.Department;
import com.roze.smarthr.entity.TrainingProgram;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.DepartmentRepository;
import com.roze.smarthr.repository.EmployeeTrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingMapper {
    private final EmployeeTrainingRepository employeeTrainingRepository;
    private final DepartmentRepository departmentRepository;

    public TrainingProgram toEntity(TrainingProgramRequest request) {
        Department department = request.getDepartmentId() != null ?
                departmentRepository.findById(request.getDepartmentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Department not found")) :
                null;

        return TrainingProgram.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .location(request.getLocation())
                .trainerName(request.getTrainerName())
                .mandatory(request.isMandatory())
                .type(request.getType())
                .department(department)
                .maxParticipants(request.getMaxParticipants())
                .build();
    }

    public TrainingProgramResponse toTrainingProgramResponse(TrainingProgram trainingProgram) {
        long enrolledCount = employeeTrainingRepository.countEnrollmentsByProgramId(trainingProgram.getId());
        long completedCount = employeeTrainingRepository.countCompletedEnrollmentsByProgramId(trainingProgram.getId());

        Department department = trainingProgram.getDepartment();
        DepartmentResponse departmentResponse = null;
        if (department != null) {

            departmentResponse = DepartmentResponse.builder()
                    .id(department.getId())
                    .title(department.getTitle())
                    .location(department.getLocation())
                    .build();
        }

        return TrainingProgramResponse.builder()
                .id(trainingProgram.getId())
                .title(trainingProgram.getTitle())
                .description(trainingProgram.getDescription())
                .startDate(trainingProgram.getStartDate())
                .endDate(trainingProgram.getEndDate())
                .location(trainingProgram.getLocation())
                .trainerName(trainingProgram.getTrainerName())
                .mandatory(trainingProgram.isMandatory())
                .type(trainingProgram.getType())
                .department(departmentResponse)
                .maxParticipants(trainingProgram.getMaxParticipants())
                .active(trainingProgram.isActive())
                .enrolledCount((int) enrolledCount)
                .completedCount((int) completedCount)
                .createdAt(trainingProgram.getCreatedAt())
                .build();
    }
}