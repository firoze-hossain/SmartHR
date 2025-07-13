package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByPayPeriod(YearMonth payPeriod);

    Optional<Payroll> findByEmployeeIdAndPayPeriod(Long employeeId, YearMonth payPeriod);

    List<Payroll> findByPayPeriodAndFinalized(YearMonth payPeriod, boolean finalized);

    boolean existsByEmployeeIdAndPayPeriod(Long id, YearMonth month);
}