
package com.roze.smarthr.repository;

import com.roze.smarthr.entity.TrainingProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long>, JpaSpecificationExecutor<TrainingProgram> {
    List<TrainingProgram> findByActiveTrueOrderByStartDateAsc();

    List<TrainingProgram> findByTypeAndActiveTrueOrderByStartDateAsc(String type);

    List<TrainingProgram> findAllByOrderByStartDateAsc();

    List<TrainingProgram> findByDepartmentIdAndActiveTrueOrderByStartDateAsc(Long departmentId);

    List<TrainingProgram> findByStartDateBetweenAndActiveTrue(LocalDate startDate, LocalDate endDate);

    @Query("SELECT tp FROM TrainingProgram tp WHERE tp.active = true AND " +
            "(LOWER(tp.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(tp.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<TrainingProgram> searchActiveTrainingPrograms(@Param("query") String query);

    @Query("SELECT tp FROM TrainingProgram tp WHERE tp.department.id = :departmentId AND " +
            "tp.active = true AND tp.startDate BETWEEN :startDate AND :endDate")
    List<TrainingProgram> findDepartmentTrainingsBetweenDates(
            @Param("departmentId") Long departmentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}