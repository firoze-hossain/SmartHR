package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Attendance;
import com.roze.smarthr.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {
    Optional<Attendance> findByEmployeeAndDate(Employee employee, LocalDate now);

    List<Attendance> findByEmployeeOrderByDateDesc(Employee employee);

    Boolean existsByEmployeeAndDate(Employee employee, LocalDate now);

    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    List<Attendance> findByDateAndLate(LocalDate yesterday, boolean b);

    List<Attendance> findByDateAndEarlyExit(LocalDate yesterday, boolean b);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.employee.id = :employeeId " +
            "AND EXTRACT(YEAR FROM a.date) = :year AND EXTRACT(MONTH FROM a.date) = :month " +
            "AND a.present = true")
    int countPresentDays(@Param("employeeId") Long employeeId,
                         @Param("year") int year,
                         @Param("month") int month);

    default int countPresentDays(Long employeeId, YearMonth month) {
        return countPresentDays(employeeId, month.getYear(), month.getMonthValue());
    }
}
