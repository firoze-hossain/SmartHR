package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.EmployeeLeaveBalanceDto;
import com.roze.smarthr.dto.LeaveBalanceUpdateDto;
import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.EmployeeLeaveBalance;
import com.roze.smarthr.entity.LeaveType;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.EmployeeLeaveBalanceMapper;
import com.roze.smarthr.repository.EmployeeLeaveBalanceRepository;
import com.roze.smarthr.repository.EmployeeRepository;
import com.roze.smarthr.repository.LeaveTypeRepository;
import com.roze.smarthr.service.EmployeeLeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeLeaveBalanceServiceImpl implements EmployeeLeaveBalanceService {
    private final EmployeeLeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeLeaveBalanceMapper balanceMapper;

    @Override
    public List<EmployeeLeaveBalanceDto> getEmployeeLeaveBalances(User user) {
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));

        return leaveBalanceRepository.findByEmployee(employee)
                .stream()
                .map(balanceMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public EmployeeLeaveBalanceDto updateLeaveBalance(Long employeeId, Long leaveTypeId, LeaveBalanceUpdateDto updateDto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + leaveTypeId));

        EmployeeLeaveBalance balance = leaveBalanceRepository
                .findByEmployeeAndLeaveType(employee, leaveType)
                .orElseGet(() -> createInitialBalance(employee, leaveType));

        // Update balance
        if (updateDto.getAdditionalQuota() != null) {
            balance.setTotalQuota(balance.getTotalQuota() + updateDto.getAdditionalQuota());
        }

        if (updateDto.getAdditionalCarryForward() != null) {
            balance.setCarriedForward(balance.getCarriedForward() + updateDto.getAdditionalCarryForward());
        }

        // Recalculate available balance
        balance.setAvailable(balance.getTotalQuota() - balance.getUsed() + balance.getCarriedForward());

        EmployeeLeaveBalance updatedBalance = leaveBalanceRepository.save(balance);
        return balanceMapper.toDto(updatedBalance);
    }

    @Override
    @Transactional
    public void processMonthlyAccrual() {
        List<Employee> employees = employeeRepository.findAll();
        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

        for (Employee employee : employees) {
            for (LeaveType leaveType : leaveTypes) {
                processLeaveAccrualForEmployee(employee, leaveType);
            }
        }
    }

    private void processLeaveAccrualForEmployee(Employee employee, LeaveType leaveType) {
        EmployeeLeaveBalance balance = leaveBalanceRepository
                .findByEmployeeAndLeaveType(employee, leaveType)
                .orElseGet(() -> createInitialBalance(employee, leaveType));

        int monthlyQuota = leaveType.getAnnualQuota() / 12;
        balance.setTotalQuota(balance.getTotalQuota() + monthlyQuota);
        
        // Recalculate available balance
        balance.setAvailable(balance.getTotalQuota() - balance.getUsed() + balance.getCarriedForward());
        
        leaveBalanceRepository.save(balance);
    }

    private EmployeeLeaveBalance createInitialBalance(Employee employee, LeaveType leaveType) {
        EmployeeLeaveBalance balance = EmployeeLeaveBalance.builder()
                .employee(employee)
                .leaveType(leaveType)
                .totalQuota(0)
                .used(0)
                .carriedForward(0)
                .available(0)
                .build();
        return leaveBalanceRepository.save(balance);
    }
}