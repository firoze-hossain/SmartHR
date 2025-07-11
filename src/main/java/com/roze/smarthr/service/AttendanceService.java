package com.roze.smarthr.service;

import com.roze.smarthr.dto.AttendanceRequest;
import com.roze.smarthr.dto.AttendanceResponse;
import com.roze.smarthr.entity.User;

import java.util.List;

public interface AttendanceService {
    AttendanceResponse checkIn(User user);
    AttendanceResponse checkOut(User user);
    List<AttendanceResponse> getMyAttendance(User user);
    List<AttendanceResponse> getEmployeeAttendance(Long employeeId);
    AttendanceResponse createAttendance(AttendanceRequest request);
}