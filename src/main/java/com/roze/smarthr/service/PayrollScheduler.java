// PayrollScheduler.java
package com.roze.smarthr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollScheduler {
    private final PayrollService payrollService;

    // Run at 10 PM on the last business day of each month
    @Scheduled(cron = "0 0 22 L * ?")
    public void autoGeneratePayrolls() {
        YearMonth currentMonth = YearMonth.now().minusMonths(1); // Process previous month
        log.info("Auto-generating payrolls for {}", currentMonth);
        payrollService.generateMonthlyPayroll(currentMonth);
    }
}