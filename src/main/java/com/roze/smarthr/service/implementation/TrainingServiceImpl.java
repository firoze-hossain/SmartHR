
package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.*;
import com.roze.smarthr.entity.*;
import com.roze.smarthr.enums.TrainingStatus;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.exception.TrainingException;
import com.roze.smarthr.mapper.EmployeeTrainingMapper;
import com.roze.smarthr.mapper.TrainingFeedbackMapper;
import com.roze.smarthr.mapper.TrainingMapper;
import com.roze.smarthr.repository.DepartmentRepository;
import com.roze.smarthr.repository.EmployeeTrainingRepository;
import com.roze.smarthr.repository.TrainingFeedbackRepository;
import com.roze.smarthr.repository.TrainingProgramRepository;
import com.roze.smarthr.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingProgramRepository trainingProgramRepository;
    private final EmployeeTrainingRepository employeeTrainingRepository;
    private final TrainingFeedbackRepository trainingFeedbackRepository;
    private final DepartmentRepository departmentRepository;
    private final TrainingMapper trainingMapper;
    private final EmployeeTrainingMapper employeeTrainingMapper;
    private final TrainingFeedbackMapper trainingFeedbackMapper;

    @Override
    @Transactional
    public TrainingProgramResponse createTrainingProgram(TrainingProgramRequest request) {
        TrainingProgram trainingProgram = trainingMapper.toEntity(request);
        TrainingProgram savedProgram = trainingProgramRepository.save(trainingProgram);
        return trainingMapper.toTrainingProgramResponse(savedProgram);
    }

    @Override
    @Transactional
    public TrainingProgramResponse updateTrainingProgram(Long id, TrainingProgramRequest request) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training program not found"));

        Department department = request.getDepartmentId() != null ?
                departmentRepository.findById(request.getDepartmentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Department not found")) :
                null;

        trainingProgram.setTitle(request.getTitle());
        trainingProgram.setDescription(request.getDescription());
        trainingProgram.setStartDate(request.getStartDate());
        trainingProgram.setEndDate(request.getEndDate());
        trainingProgram.setLocation(request.getLocation());
        trainingProgram.setTrainerName(request.getTrainerName());
        trainingProgram.setMandatory(request.isMandatory());
        trainingProgram.setType(request.getType());
        trainingProgram.setDepartment(department);
        trainingProgram.setMaxParticipants(request.getMaxParticipants());
        trainingProgram.setUpdatedAt(LocalDate.now());

        TrainingProgram updatedProgram = trainingProgramRepository.save(trainingProgram);
        return trainingMapper.toTrainingProgramResponse(updatedProgram);
    }

    @Override
    @Transactional
    public void deactivateTrainingProgram(Long id) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training program not found"));
        trainingProgram.setActive(false);
        trainingProgram.setUpdatedAt(LocalDate.now());
        trainingProgramRepository.save(trainingProgram);
    }

    @Override
    public TrainingProgramResponse getTrainingProgramById(Long id) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Training program not found"));
        return trainingMapper.toTrainingProgramResponse(trainingProgram);
    }

    @Override
    public List<TrainingProgramResponse> getAllTrainingPrograms(boolean activeOnly) {
        List<TrainingProgram> programs = activeOnly ?
                trainingProgramRepository.findByActiveTrueOrderByStartDateAsc() :
                trainingProgramRepository.findAllByOrderByStartDateAsc();
        return programs.stream()
                .map(trainingMapper::toTrainingProgramResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingProgramResponse> getTrainingProgramsByType(String type) {
        List<TrainingProgram> programs = trainingProgramRepository.findByTypeAndActiveTrueOrderByStartDateAsc(type);
        return programs.stream()
                .map(trainingMapper::toTrainingProgramResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingProgramResponse> getTrainingProgramsByDepartment(Long departmentId) {
        List<TrainingProgram> programs = trainingProgramRepository.findByDepartmentIdAndActiveTrueOrderByStartDateAsc(departmentId);
        return programs.stream()
                .map(trainingMapper::toTrainingProgramResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeTrainingResponse enrollEmployee(EmployeeTrainingRequest request) {
        EmployeeTraining employeeTraining = employeeTrainingMapper.toEntity(request);

        if (!employeeTraining.getTrainingProgram().isActive()) {
            throw new TrainingException("Cannot enroll in an inactive training program");
        }

        if (employeeTrainingRepository.existsByEmployeeIdAndTrainingProgramId(
                employeeTraining.getEmployee().getId(),
                employeeTraining.getTrainingProgram().getId())) {
            throw new TrainingException("Employee is already enrolled in this training program");
        }

        long currentEnrollments = employeeTrainingRepository.countByTrainingProgramId(
                employeeTraining.getTrainingProgram().getId());
        if (currentEnrollments >= employeeTraining.getTrainingProgram().getMaxParticipants()) {
            throw new TrainingException("Training program has reached maximum participants");
        }

        EmployeeTraining savedEnrollment = employeeTrainingRepository.save(employeeTraining);
        return employeeTrainingMapper.toEmployeeTrainingResponse(savedEnrollment);
    }

    @Override
    @Transactional
    public EmployeeTrainingResponse updateEmployeeTrainingStatus(Long id, EmployeeTrainingRequest request) {
        EmployeeTraining employeeTraining = employeeTrainingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee training record not found"));

        if (request.getStatus() != null) {
            employeeTraining.setStatus(request.getStatus());
            if (request.getStatus() == TrainingStatus.COMPLETED) {
                employeeTraining.setCompletionDate(LocalDate.now());
            }
        }

        if (request.getScore() != null) {
            employeeTraining.setScore(request.getScore());
        }

        if (request.getCertificateId() != null) {
            employeeTraining.setCertificateId(request.getCertificateId());
        }

        EmployeeTraining updatedEnrollment = employeeTrainingRepository.save(employeeTraining);
        return employeeTrainingMapper.toEmployeeTrainingResponse(updatedEnrollment);
    }

    @Override
    public List<EmployeeTrainingResponse> getEmployeeTrainings(Long employeeId) {
        List<EmployeeTraining> trainings = employeeTrainingRepository.findByEmployeeIdOrderByEnrolledDateDesc(employeeId);
        return trainings.stream()
                .map(employeeTrainingMapper::toEmployeeTrainingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeTrainingResponse> getTrainingProgramParticipants(Long programId) {
        List<EmployeeTraining> participants = employeeTrainingRepository.findByTrainingProgramIdOrderByEmployeeNameAsc(programId);
        return participants.stream()
                .map(employeeTrainingMapper::toEmployeeTrainingResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TrainingFeedbackResponse submitFeedback(TrainingFeedbackRequest request, User user) {
        EmployeeTraining employeeTraining = employeeTrainingRepository.findById(request.getEmployeeTrainingId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee training record not found"));

        if (!employeeTraining.getEmployee().getUser().getId().equals(user.getId())) {
            throw new TrainingException("You can only submit feedback for your own training records");
        }

        if (employeeTraining.getStatus() != TrainingStatus.COMPLETED) {
            throw new TrainingException("Feedback can only be submitted for completed trainings");
        }

        if (trainingFeedbackRepository.existsByEmployeeTraining(employeeTraining)) {
            throw new TrainingException("Feedback already submitted for this training");
        }

        TrainingFeedback feedback = TrainingFeedback.builder()
                .employeeTraining(employeeTraining)
                .rating(request.getRating())
                .comments(request.getComments())
                .submittedDate(LocalDate.now())
                .trainerFeedback(request.isTrainerFeedback())
                .build();

        TrainingFeedback savedFeedback = trainingFeedbackRepository.save(feedback);
        employeeTraining.setFeedbackSubmitted(true);
        employeeTrainingRepository.save(employeeTraining);

        return trainingFeedbackMapper.toTrainingFeedbackResponse(savedFeedback);
    }

    @Override
    public List<TrainingFeedbackResponse> getFeedbackForTraining(Long trainingProgramId) {
        List<TrainingFeedback> feedbacks = trainingFeedbackRepository.findByEmployeeTraining_TrainingProgramId(trainingProgramId);
        return feedbacks.stream()
                .map(trainingFeedbackMapper::toTrainingFeedbackResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingFeedbackResponse> getFeedbackForTrainer(String trainerName) {
        List<TrainingFeedback> feedbacks = trainingFeedbackRepository.findByEmployeeTraining_TrainingProgram_TrainerNameAndTrainerFeedback(trainerName, true);
        return feedbacks.stream()
                .map(trainingFeedbackMapper::toTrainingFeedbackResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingProgramResponse> getUpcomingMandatoryTrainings(Long employeeId) {
        List<EmployeeTraining> incompleteMandatory = employeeTrainingRepository.findIncompleteMandatoryTrainings(employeeId);
        return incompleteMandatory.stream()
                .map(et -> trainingMapper.toTrainingProgramResponse(et.getTrainingProgram()))
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeTrainingResponse> getIncompleteMandatoryTrainings(Long employeeId) {
        List<EmployeeTraining> incompleteMandatory = employeeTrainingRepository.findIncompleteMandatoryTrainings(employeeId);
        return incompleteMandatory.stream()
                .map(employeeTrainingMapper::toEmployeeTrainingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingProgramResponse> getUpcomingDepartmentTrainings(Long departmentId) {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);

        List<TrainingProgram> programs = trainingProgramRepository.findDepartmentTrainingsBetweenDates(
                departmentId, today, nextMonth);

        return programs.stream()
                .map(trainingMapper::toTrainingProgramResponse)
                .collect(Collectors.toList());
    }
}