package com.roze.smarthr.service;

import com.roze.smarthr.dto.FinalizePayrollRequest;
import com.roze.smarthr.dto.PayrollResponse;

import java.time.YearMonth;
import java.util.List;

public interface PayrollService {
    void generateMonthlyPayroll(YearMonth month);

    List<PayrollResponse> getPayrollsForMonth(YearMonth month);

    PayrollResponse getPayroll(Long id);

    void finalizePayroll(Long payrollId, FinalizePayrollRequest request);
}