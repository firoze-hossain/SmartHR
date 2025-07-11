package com.roze.smarthr.service;

import com.roze.smarthr.dto.LeaveRequestDto;
import com.roze.smarthr.dto.LeaveResponseDto;
import com.roze.smarthr.dto.LeaveStatusUpdateDto;
import com.roze.smarthr.entity.User;

import java.util.List;

public interface LeaveService {
    LeaveResponseDto applyForLeave(LeaveRequestDto request, User user);
    List<LeaveResponseDto> getMyLeaves(User user);
    List<LeaveResponseDto> getAllLeaves();
    LeaveResponseDto updateLeaveStatus(Long leaveId, LeaveStatusUpdateDto statusUpdate);
}