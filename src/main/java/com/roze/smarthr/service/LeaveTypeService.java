package com.roze.smarthr.service;

import com.roze.smarthr.dto.LeaveTypeDto;
import com.roze.smarthr.dto.LeaveTypeResponseDto;

import java.util.List;

public interface LeaveTypeService {
    LeaveTypeResponseDto createLeaveType(LeaveTypeDto leaveTypeDto);
    List<LeaveTypeResponseDto> getAllLeaveTypes();
    LeaveTypeResponseDto getLeaveTypeById(Long id);
    LeaveTypeResponseDto updateLeaveType(Long id, LeaveTypeDto leaveTypeDto);
    void deleteLeaveType(Long id);
}