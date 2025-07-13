package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.RosterAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RosterAssignmentRepository extends JpaRepository<RosterAssignment, Long> {
    List<RosterAssignment> findByEmployeeAndAssignmentDateBetween(Employee employee, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT ra FROM RosterAssignment ra " +
           "WHERE ra.employee.department.id = :departmentId " +
           "AND ra.assignmentDate BETWEEN :startDate AND :endDate")
    List<RosterAssignment> findByDepartmentAndDateRange(Long departmentId, LocalDate startDate, LocalDate endDate);
    
    boolean existsByEmployeeIdAndAssignmentDate(Long employeeId, LocalDate date);

   Optional<RosterAssignment> findByEmployeeIdAndAssignmentDate(Long employeeId, LocalDate date);
}