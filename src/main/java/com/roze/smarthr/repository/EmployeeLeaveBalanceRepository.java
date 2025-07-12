package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.EmployeeLeaveBalance;
import com.roze.smarthr.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EmployeeLeaveBalanceRepository extends JpaRepository<EmployeeLeaveBalance, Long>, JpaSpecificationExecutor<EmployeeLeaveBalance> {
    Optional<EmployeeLeaveBalance> findByEmployeeAndLeaveType(Employee employee, LeaveType leaveType);

    List<EmployeeLeaveBalance> findByEmployee(Employee employee);

    boolean existsByLeaveType(LeaveType leaveType);

    @Modifying
    @Transactional
    @Query("UPDATE EmployeeLeaveBalance elb " +
            "SET elb.used = elb.used + :daysUsed, " +
            "elb.available = elb.totalQuota - (elb.used + :daysUsed) + elb.carriedForward " +
            "WHERE elb.employee.id = :employeeId AND elb.leaveType.id = :leaveTypeId")
    void incrementUsedDays(@Param("employeeId") Long employeeId,
                           @Param("leaveTypeId") Long leaveTypeId,
                           @Param("daysUsed") int daysUsed);

    @Modifying
    @Query("DELETE FROM EmployeeLeaveBalance elb WHERE elb.employee = :employee AND elb.leaveType NOT IN :leaveTypes")
    void deleteByEmployeeAndLeaveTypeNotIn(@Param("employee") Employee employee,
                                           @Param("leaveTypes") List<LeaveType> leaveTypes);

    boolean existsByEmployeeAndLeaveType(Employee employee, LeaveType leaveType);

    @Modifying
    @Transactional
    @Query("UPDATE EmployeeLeaveBalance elb SET elb.used = elb.used - :daysUsed " +
            "WHERE elb.employee.id = :employeeId AND elb.leaveType.id = :leaveTypeId")
    void decrementUsedDays(@Param("employeeId") Long employeeId,
                           @Param("leaveTypeId") Long leaveTypeId,
                           @Param("daysUsed") int daysUsed);
}
