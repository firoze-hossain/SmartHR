package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.LeaveRequestDto;
import com.roze.smarthr.dto.LeaveResponseDto;
import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.LeaveRequest;
import com.roze.smarthr.enums.LeaveStatus;
import org.springframework.stereotype.Service;

@Service
public class LeaveMapper {
    public LeaveRequest toEntity(LeaveRequestDto requestDto, Employee employee) {
        return LeaveRequest.builder()
                .employee(employee)
                .fromDate(requestDto.getFromDate())
                .toDate(requestDto.getToDate())
                .reason(requestDto.getReason())
                .status(LeaveStatus.PENDING)
                .build();
    }

    public LeaveResponseDto toDto(LeaveRequest leaveRequest) {
        return LeaveResponseDto.builder()
                .id(leaveRequest.getId())
                .employeeId(leaveRequest.getEmployee().getId())
                .employeeName(leaveRequest.getEmployee().getName())
                .fromDate(leaveRequest.getFromDate())
                .toDate(leaveRequest.getToDate())
                .reason(leaveRequest.getReason())
                .status(leaveRequest.getStatus())
                .build();
    }
}