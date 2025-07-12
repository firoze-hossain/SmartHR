package com.roze.smarthr.service;

import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.EmployeeLeaveBalance;
import com.roze.smarthr.entity.LeaveType;
import com.roze.smarthr.repository.EmployeeLeaveBalanceRepository;
import com.roze.smarthr.repository.EmployeeRepository;
import com.roze.smarthr.repository.LeaveTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveAccrualService {
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeLeaveBalanceRepository leaveBalanceRepository;

    @Scheduled(cron = "0 0 0 1 * ?") // Runs at midnight on the 1st of every month
    @Transactional
    public void processMonthlyAccrual() {
        List<Employee> employees = employeeRepository.findAll();
        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Employee employee : employees) {
            for (LeaveType leaveType : leaveTypes) {
                processLeaveAccrualForEmployee(employee, leaveType, today);
            }
        }
    }

    private void processLeaveAccrualForEmployee(Employee employee, LeaveType leaveType, LocalDate processDate) {
        EmployeeLeaveBalance balance = leaveBalanceRepository
                .findByEmployeeAndLeaveType(employee, leaveType)
                .orElse(createInitialBalance(employee, leaveType));

        int monthlyQuota = leaveType.getAnnualQuota() / 12;
        int newTotalQuota = balance.getTotalQuota() + monthlyQuota;

        // Handle carry forward at year end
        if (processDate.getMonthValue() == 1) { // January
            if (leaveType.isCarryForwardAllowed()) {
                int carryForward = Math.min(balance.getAvailable(), leaveType.getMaxCarryForwardDays());
                balance.setCarriedForward(carryForward);
            } else {
                balance.setCarriedForward(0);
            }
            // Reset used days for new year
            balance.setUsed(0);
        }

        balance.setTotalQuota(newTotalQuota);
        balance.setAvailable(newTotalQuota - balance.getUsed() + balance.getCarriedForward());
        leaveBalanceRepository.save(balance);
    }

    private EmployeeLeaveBalance createInitialBalance(Employee employee, LeaveType leaveType) {
        return EmployeeLeaveBalance.builder()
                .employee(employee)
                .leaveType(leaveType)
                .totalQuota(0)
                .used(0)
                .carriedForward(0)
                .available(0)
                .build();
    }
}