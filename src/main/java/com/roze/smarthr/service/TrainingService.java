
package com.roze.smarthr.service;

import com.roze.smarthr.dto.*;
import com.roze.smarthr.entity.User;

import java.util.List;

public interface TrainingService {
    TrainingProgramResponse createTrainingProgram(TrainingProgramRequest request);

    TrainingProgramResponse updateTrainingProgram(Long id, TrainingProgramRequest request);

    void deactivateTrainingProgram(Long id);

    TrainingProgramResponse getTrainingProgramById(Long id);

    List<TrainingProgramResponse> getAllTrainingPrograms(boolean activeOnly);

    List<TrainingProgramResponse> getTrainingProgramsByType(String type);

    List<TrainingProgramResponse> getTrainingProgramsByDepartment(Long departmentId);

    EmployeeTrainingResponse enrollEmployee(EmployeeTrainingRequest request);

    EmployeeTrainingResponse updateEmployeeTrainingStatus(Long id, EmployeeTrainingRequest request);

    List<EmployeeTrainingResponse> getEmployeeTrainings(Long employeeId);

    List<EmployeeTrainingResponse> getTrainingProgramParticipants(Long programId);

    TrainingFeedbackResponse submitFeedback(TrainingFeedbackRequest request, User user);

    List<TrainingFeedbackResponse> getFeedbackForTraining(Long trainingProgramId);

    List<TrainingFeedbackResponse> getFeedbackForTrainer(String trainerName);

    List<TrainingProgramResponse> getUpcomingMandatoryTrainings(Long employeeId);

    List<EmployeeTrainingResponse> getIncompleteMandatoryTrainings(Long employeeId);

    List<TrainingProgramResponse> getUpcomingDepartmentTrainings(Long departmentId);
}