package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.FinalizePayrollRequest;
import com.roze.smarthr.dto.PayrollResponse;
import com.roze.smarthr.entity.*;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.PayrollMapper;
import com.roze.smarthr.repository.*;
import com.roze.smarthr.service.PayrollService;
import com.roze.smarthr.service.PayslipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollServiceImpl implements PayrollService {
    private final EmployeeRepository employeeRepository;
    private final SalaryStructureRepository salaryStructureRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final PayrollRepository payrollRepository;
    private final PayslipService payslipService;
    private final PayrollMapper payrollMapper;

    @Override
    @Transactional
    public void generateMonthlyPayroll(YearMonth month) {
        List<Employee> activeEmployees = employeeRepository.findActiveEmployees();
        
        activeEmployees.forEach(employee -> {
            if (payrollRepository.existsByEmployeeIdAndPayPeriod(employee.getId(), month)) {
                log.warn("Payroll already exists for employee {} for {}", employee.getId(), month);
                return;
            }
            
            SalaryStructure salary = salaryStructureRepository.findByEmployeeId(employee.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Salary structure not found for employee: " + employee.getId()));
            
            Payroll payroll = calculatePayroll(employee, salary, month);
            payrollRepository.save(payroll);
            log.info("Generated payroll for {}: {}", employee.getName(), payroll.getId());
        });
    }

    private Payroll calculatePayroll(Employee employee, SalaryStructure salary, YearMonth month) {
        int workingDays = calculateWorkingDays(month);
        int presentDays = attendanceRepository.countPresentDays(employee.getId(), month);
        int paidLeaves = leaveRequestRepository.countApprovedLeaveDays(employee.getId(), month);
        int unpaidLeaves = workingDays - presentDays - paidLeaves;
        unpaidLeaves = Math.max(unpaidLeaves, 0);
        
        BigDecimal perDaySalary = salary.getGrossSalary()
            .divide(BigDecimal.valueOf(workingDays), 2, RoundingMode.HALF_UP);
        
        BigDecimal lopAmount = perDaySalary.multiply(BigDecimal.valueOf(unpaidLeaves));
        BigDecimal netPayable = salary.getGrossSalary().subtract(lopAmount);
        
        return Payroll.builder()
            .employee(employee)
            .payPeriod(month)
            .workingDays(workingDays)
            .presentDays(presentDays)
            .paidLeaves(paidLeaves)
            .unpaidLeaves(unpaidLeaves)
            .grossSalary(salary.getGrossSalary())
            .lopAmount(lopAmount)
            .netPayable(netPayable)
            .approved(false)
            .finalized(false)
            .generatedAt(LocalDateTime.now())
            .build();
    }
    
    private int calculateWorkingDays(YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();
        
        int workingDays = 0;
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && 
                date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                workingDays++;
            }
        }
        return workingDays;
    }

    @Override
    public List<PayrollResponse> getPayrollsForMonth(YearMonth month) {
        return payrollRepository.findByPayPeriod(month).stream()
                .map(payrollMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PayrollResponse getPayroll(Long id) {
        Payroll payroll = payrollRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found"));
        return payrollMapper.toDto(payroll);
    }

    @Override
    @Transactional
    public void finalizePayroll(Long payrollId, FinalizePayrollRequest request) {
        Payroll payroll = payrollRepository.findById(payrollId)
            .orElseThrow(() -> new ResourceNotFoundException("Payroll not found"));
        
        payroll.setFinalized(true);
        payroll.setApproved(true);
        payroll.setRemarks(request.getRemarks());
        
        // Generate payslip PDF
        byte[] pdf = payslipService.generatePayslipPdf(payrollId);
        // Save PDF logic here
        
        payrollRepository.save(payroll);
    }
}