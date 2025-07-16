
package com.roze.smarthr.repository;

import com.roze.smarthr.entity.EmployeeTraining;
import com.roze.smarthr.entity.TrainingFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TrainingFeedbackRepository extends JpaRepository<TrainingFeedback, Long> {
    List<TrainingFeedback> findByEmployeeTrainingId(Long employeeTrainingId);

    boolean existsByEmployeeTraining(EmployeeTraining employeeTraining);

    List<TrainingFeedback> findByTrainerFeedback(boolean trainerFeedback);

    @Query("SELECT tf FROM TrainingFeedback tf WHERE tf.employeeTraining.trainingProgram.id = :trainingProgramId")
    List<TrainingFeedback> findByEmployeeTraining_TrainingProgramId(@Param("trainingProgramId") Long trainingProgramId);

    @Query("SELECT tf FROM TrainingFeedback tf WHERE " +
            "tf.employeeTraining.trainingProgram.trainerName = :trainerName AND " +
            "tf.trainerFeedback = :trainerFeedback")
    List<TrainingFeedback> findByEmployeeTraining_TrainingProgram_TrainerNameAndTrainerFeedback(
            @Param("trainerName") String trainerName,
            @Param("trainerFeedback") boolean trainerFeedback);
}