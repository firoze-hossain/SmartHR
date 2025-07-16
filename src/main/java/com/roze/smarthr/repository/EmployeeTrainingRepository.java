
package com.roze.smarthr.repository;

import com.roze.smarthr.entity.EmployeeTraining;
import com.roze.smarthr.enums.TrainingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeTrainingRepository extends JpaRepository<EmployeeTraining, Long> {
    List<EmployeeTraining> findByEmployeeIdOrderByEnrolledDateDesc(Long employeeId);

    List<EmployeeTraining> findByTrainingProgramIdOrderByEmployeeNameAsc(Long trainingProgramId);

    Optional<EmployeeTraining> findByEmployeeIdAndTrainingProgramId(Long employeeId, Long trainingProgramId);

    List<EmployeeTraining> findByStatusAndTrainingProgramEndDateBefore(TrainingStatus status, LocalDate date);

    @Query("SELECT COUNT(et) FROM EmployeeTraining et WHERE et.trainingProgram.id = :programId")
    long countByTrainingProgramId(@Param("programId") Long programId);

    @Query("SELECT COUNT(et) FROM EmployeeTraining et WHERE et.trainingProgram.id = :programId AND et.status = 'COMPLETED'")
    long countByTrainingProgramIdAndStatusCompleted(@Param("programId") Long programId);

    @Query("SELECT et FROM EmployeeTraining et WHERE et.employee.id = :employeeId AND " +
            "et.trainingProgram.type = :trainingType AND et.status = 'COMPLETED'")
    List<EmployeeTraining> findCompletedTrainingsByEmployeeAndType(
            @Param("employeeId") Long employeeId,
            @Param("trainingType") String trainingType);

    @Query("SELECT et FROM EmployeeTraining et WHERE et.employee.id = :employeeId AND " +
            "et.trainingProgram.mandatory = true AND et.status != 'COMPLETED'")
    List<EmployeeTraining> findIncompleteMandatoryTrainings(@Param("employeeId") Long employeeId);

    boolean existsByEmployeeIdAndTrainingProgramId(Long employeeId, Long trainingProgramId);

    @Query("SELECT et FROM EmployeeTraining et WHERE et.status = :status AND et.trainingProgram.startDate = :date")
    List<EmployeeTraining> findByStatusAndTrainingProgram_StartDate(
            @Param("status") TrainingStatus status,
            @Param("date") LocalDate date);

    @Query("SELECT et FROM EmployeeTraining et WHERE et.status = :status AND et.completionDate = :date AND et.feedbackSubmitted = false")
    List<EmployeeTraining> findByStatusAndCompletionDateAndFeedbackSubmittedFalse(
            @Param("status") TrainingStatus status,
            @Param("date") LocalDate date);

    @Query("SELECT et FROM EmployeeTraining et WHERE et.trainingProgram.mandatory = :mandatory AND et.status != :status")
    List<EmployeeTraining> findByTrainingProgram_MandatoryAndStatusNot(
            @Param("mandatory") boolean mandatory,
            @Param("status") TrainingStatus status);

    default long countEnrollmentsByProgramId(Long programId) {
        return countByTrainingProgramId(programId);
    }

    default long countCompletedEnrollmentsByProgramId(Long programId) {
        return countByTrainingProgramIdAndStatusCompleted(programId);
    }
}