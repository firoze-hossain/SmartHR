package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.AttendanceRequest;
import com.roze.smarthr.dto.AttendanceResponse;
import com.roze.smarthr.entity.Attendance;
import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.exception.AttendanceException;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.AttendanceMapper;
import com.roze.smarthr.repository.AttendanceRepository;
import com.roze.smarthr.repository.EmployeeRepository;
import com.roze.smarthr.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceMapper attendanceMapper;

    @Override
    public AttendanceResponse checkIn(User user) {
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));
        if (attendanceRepository.existsByEmployeeAndDate(employee, LocalDate.now())) {
            throw new AttendanceException("You have already checked in today");
        }

        AttendanceRequest request = new AttendanceRequest();
        request.setEmployeeId(employee.getId());
        request.setDate(LocalDate.now());

        Attendance attendance = attendanceMapper.toCheckInEntity(request, employee);
        Attendance savedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toDto(savedAttendance);
    }

    @Override
    public AttendanceResponse checkOut(User user) {
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));

        Attendance attendance = attendanceRepository.findByEmployeeAndDate(employee, LocalDate.now())
                .orElseThrow(() -> new AttendanceException("You need to check in first"));

        if (attendance.getCheckOut() != null) {
            throw new AttendanceException("You have already checked out today");
        }

        Attendance updatedAttendance = attendanceMapper.toCheckOutEntity(attendance);
        Attendance savedAttendance = attendanceRepository.save(updatedAttendance);
        return attendanceMapper.toDto(savedAttendance);
    }

    @Override
    public List<AttendanceResponse> getMyAttendance(User user) {
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user"));

        return attendanceRepository.findByEmployeeOrderByDateDesc(employee)
                .stream()
                .map(attendanceMapper::toDto)
                .toList();
    }

    @Override
    public List<AttendanceResponse> getEmployeeAttendance(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        return attendanceRepository.findByEmployeeOrderByDateDesc(employee)
                .stream()
                .map(attendanceMapper::toDto)
                .toList();
    }

    @Override
    public AttendanceResponse createAttendance(AttendanceRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + request.getEmployeeId()));

        if (attendanceRepository.existsByEmployeeAndDate(employee, request.getDate())) {
            throw new AttendanceException("Attendance already exists for this employee on " + request.getDate());
        }

        Attendance attendance = Attendance.builder()
                .employee(employee)
                .date(request.getDate())
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .present(request.getCheckIn() != null)
                .build();

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toDto(savedAttendance);
    }
}