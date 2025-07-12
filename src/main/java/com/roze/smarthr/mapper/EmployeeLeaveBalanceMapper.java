package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.EmployeeLeaveBalanceDto;
import com.roze.smarthr.entity.EmployeeLeaveBalance;
import org.springframework.stereotype.Service;

@Service
public class EmployeeLeaveBalanceMapper {
    public EmployeeLeaveBalanceDto toDto(EmployeeLeaveBalance balance) {
        return EmployeeLeaveBalanceDto.builder()
                .leaveTypeId(balance.getLeaveType().getId())
                .leaveTypeName(balance.getLeaveType().getName())
                .totalQuota(balance.getTotalQuota())
                .used(balance.getUsed())
                .carriedForward(balance.getCarriedForward())
                //.available(balance.getAvailable())
                .build();
    }
}