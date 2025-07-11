package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.LeaveRequest;
import com.roze.smarthr.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>, JpaSpecificationExecutor<LeaveRequest> {
    List<LeaveRequest> findByEmployeeOrderByFromDateDesc(Employee employee);

    List<LeaveRequest> findAllByOrderByFromDateDesc();

    Boolean existsByEmployeeAndStatusAndFromDateLessThanEqualAndToDateGreaterThanEqual(Employee employee, LeaveStatus approved, LocalDate toDate, LocalDate fromDate);
}
