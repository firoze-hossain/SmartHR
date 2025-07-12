package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.LeaveTypeDto;
import com.roze.smarthr.dto.LeaveTypeResponseDto;
import com.roze.smarthr.entity.LeaveType;
import org.springframework.stereotype.Service;

@Service
public class LeaveTypeMapper {
    public LeaveType toEntity(LeaveTypeDto dto) {
        return LeaveType.builder()
                .name(dto.getName())
                .annualQuota(dto.getAnnualQuota())
                .carryForwardAllowed(dto.getCarryForwardAllowed() != null ? dto.getCarryForwardAllowed() : false)
                .maxCarryForwardDays(dto.getMaxCarryForwardDays() != null ? dto.getMaxCarryForwardDays() : 0)
                .build();
    }

    public LeaveTypeResponseDto toDto(LeaveType leaveType) {
        return LeaveTypeResponseDto.builder()
                .id(leaveType.getId())
                .name(leaveType.getName())
                .annualQuota(leaveType.getAnnualQuota())
                .carryForwardAllowed(leaveType.isCarryForwardAllowed())
                .maxCarryForwardDays(leaveType.getMaxCarryForwardDays())
                .build();
    }
}