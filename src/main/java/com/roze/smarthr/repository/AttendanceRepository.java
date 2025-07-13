package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Attendance;
import com.roze.smarthr.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {
    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate now);

    List<Attendance> findByEmployeeOrderByDateDesc(Employee employee);

    Boolean existsByEmployeeAndDate(Employee employee, LocalDate now);

    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
}
