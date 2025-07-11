package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.AttendanceRequest;
import com.roze.smarthr.dto.AttendanceResponse;
import com.roze.smarthr.entity.Attendance;
import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceMapper {
    private final EmployeeRepository employeeRepository;

    public Attendance toCheckInEntity(AttendanceRequest request, Employee employee) {
        return Attendance.builder()
                .employee(employee)
                .date(LocalDate.now())
                .checkIn(LocalDateTime.now())
                .present(true)
                .build();
    }

    public Attendance toCheckOutEntity(Attendance existingAttendance) {
        existingAttendance.setCheckOut(LocalDateTime.now());
        return existingAttendance;
    }

    public AttendanceResponse toDto(Attendance attendance) {
        String status = determineStatus(attendance);
        
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .employeeId(attendance.getEmployee().getId())
                .employeeName(attendance.getEmployee().getName())
                .date(attendance.getDate())
                .checkIn(attendance.getCheckIn())
                .checkOut(attendance.getCheckOut())
                .present(attendance.isPresent())
                .status(status)
                .build();
    }

    private String determineStatus(Attendance attendance) {
        if (!attendance.isPresent()) return "Absent";
        if (attendance.getCheckOut() == null) return "Incomplete";
        return "Present";
    }
}