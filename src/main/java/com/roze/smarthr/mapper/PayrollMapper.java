package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.PayrollResponse;
import com.roze.smarthr.entity.Payroll;
import org.springframework.stereotype.Service;

@Service
public class PayrollMapper {
    public PayrollResponse toDto(Payroll payroll) {
        return PayrollResponse.builder()
                .id(payroll.getId())
                .employeeId(payroll.getEmployee().getId())
                .employeeName(payroll.getEmployee().getName())
                .payPeriod(payroll.getPayPeriod())
                .workingDays(payroll.getWorkingDays())
                .presentDays(payroll.getPresentDays())
                .paidLeaves(payroll.getPaidLeaves())
                .unpaidLeaves(payroll.getUnpaidLeaves())
                .grossSalary(payroll.getGrossSalary())
                .lopAmount(payroll.getLopAmount())
                .netPayable(payroll.getNetPayable())
                .approved(payroll.isApproved())
                .finalized(payroll.isFinalized())
                .generatedAt(payroll.getGeneratedAt())
                .remarks(payroll.getRemarks())
                .payslipUrl(payroll.getPayslip() != null ? "/api/v1/payslips/" + payroll.getId() : null)
                .build();
    }
}