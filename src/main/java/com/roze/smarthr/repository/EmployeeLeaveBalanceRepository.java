package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.EmployeeLeaveBalance;
import com.roze.smarthr.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeLeaveBalanceRepository extends JpaRepository<EmployeeLeaveBalance, Long>, JpaSpecificationExecutor<EmployeeLeaveBalance> {
    Optional<EmployeeLeaveBalance> findByEmployeeAndLeaveType(Employee employee, LeaveType leaveType);

    Optional<EmployeeLeaveBalance> findByEmployee(Employee employee);

    boolean existsByLeaveType(LeaveType leaveType);

    @Modifying
    @Query("UPDATE EmployeeLeaveBalance elb SET" +
            " elb.used = elb.used + :daysUsed WHERE " +
            "elb.employee.id = :employeeId AND elb.leaveType.id = :leaveTypeId")
    void incrementUsedDays(@Param("employeeId") Long employeeId,
                           @Param("leaveTypeId") Long leaveTypeId,
                           @Param("daysUsed") int daysUsed);
}
