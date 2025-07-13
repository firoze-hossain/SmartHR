package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.LeaveRequest;
import com.roze.smarthr.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>, JpaSpecificationExecutor<LeaveRequest> {
    List<LeaveRequest> findByEmployeeOrderByFromDateDesc(Employee employee);

    List<LeaveRequest> findAllByOrderByFromDateDesc();

    Boolean existsByEmployeeAndStatusAndFromDateLessThanEqualAndToDateGreaterThanEqual(Employee employee, LeaveStatus approved, LocalDate toDate, LocalDate fromDate);

    @Query("SELECT COUNT(lr) FROM LeaveRequest lr " +
            "WHERE :date BETWEEN lr.fromDate AND lr.toDate " +
            "AND lr.status = 'APPROVED'")
    int countEmployeesOnLeave(@Param("date") LocalDate date);

    @Query("SELECT COUNT(lr) FROM LeaveRequest lr " +
            "WHERE MONTH(lr.fromDate) = :month OR MONTH(lr.toDate) = :month")
    int countByMonth(@Param("month") int month);

    int countByStatus(LeaveStatus status);

    List<LeaveRequest> findByStatusOrderByFromDateAsc(LeaveStatus status);

    List<LeaveRequest> findByStatusAndFromDate(LeaveStatus status, LocalDate fromDate);

    long countByEmployeeAndStatusAndFromDateBetween(Employee employee, LeaveStatus status, LocalDate start, LocalDate end);

    @Query("SELECT COALESCE(SUM(EXTRACT(DAY FROM l.toDate) - EXTRACT(DAY FROM l.fromDate) + 1), 0) " +
            "FROM LeaveRequest l " +
            "WHERE l.employee.id = :employeeId " +
            "AND EXTRACT(YEAR FROM l.fromDate) = :year " +
            "AND EXTRACT(MONTH FROM l.fromDate) = :month " +
            "AND l.status = 'APPROVED'")
    int countApprovedLeaveDays(@Param("employeeId") Long employeeId,
                               @Param("year") int year,
                               @Param("month") int month);

    default int countApprovedLeaveDays(Long employeeId, YearMonth month) {
        return countApprovedLeaveDays(employeeId, month.getYear(), month.getMonthValue());
    }
}
