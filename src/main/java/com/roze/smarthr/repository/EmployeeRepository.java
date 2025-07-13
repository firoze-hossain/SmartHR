package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    Boolean existsByUserId(Long userId);

    Optional<Employee> findByUser(User user);

    @Query("SELECT e FROM Employee e WHERE EXTRACT(MONTH FROM e.birthDate) = :month AND EXTRACT(DAY FROM e.birthDate) = :day")
    List<Employee> findByBirthDate(@Param("month") int month, @Param("day") int day);

    @Query("SELECT e FROM Employee e JOIN RosterAssignment ra ON e.id = ra.employee.id " +
            "WHERE ra.assignmentDate = :date AND ra.isDayOff = false")
    List<Employee> findEmployeesWithShiftOnDate(@Param("date") LocalDate date);

    @Query("SELECT e FROM Employee e WHERE e.user.enabled = true")
    List<Employee> findActiveEmployees();
}
