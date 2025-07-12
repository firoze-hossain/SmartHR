package com.roze.smarthr.service;

import com.roze.smarthr.dto.EmployeeLeaveBalanceDto;
import com.roze.smarthr.dto.LeaveBalanceUpdateDto;
import com.roze.smarthr.entity.User;

import java.util.List;

public interface EmployeeLeaveBalanceService {
    List<EmployeeLeaveBalanceDto> getEmployeeLeaveBalances(User user);
    EmployeeLeaveBalanceDto updateLeaveBalance(Long employeeId, Long leaveTypeId, LeaveBalanceUpdateDto updateDto);
    void processMonthlyAccrual();
}