package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.*;
import com.roze.smarthr.entity.*;
import com.roze.smarthr.exception.*;
import com.roze.smarthr.mapper.*;
import com.roze.smarthr.repository.*;
import com.roze.smarthr.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {
    private final ShiftTemplateRepository shiftTemplateRepository;
    private final RosterAssignmentRepository rosterAssignmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final AttendanceRepository attendanceRepository;
    private final ShiftTemplateMapper shiftTemplateMapper;
    private final RosterAssignmentMapper rosterAssignmentMapper;

    @Override
    @Transactional
    public ShiftTemplateDto createShiftTemplate(ShiftTemplateDto shiftTemplateDto) {
        if (shiftTemplateRepository.existsByName(shiftTemplateDto.getName())) {
            throw new DuplicateResourceException("Shift template with this name already exists");
        }
        
        ShiftTemplate shiftTemplate = shiftTemplateMapper.toEntity(shiftTemplateDto);
        ShiftTemplate savedTemplate = shiftTemplateRepository.save(shiftTemplate);
        return shiftTemplateMapper.toDto(savedTemplate);
    }

    @Override
    public List<ShiftTemplateDto> getAllActiveShiftTemplates() {
        return shiftTemplateRepository.findByActiveTrue()
                .stream()
                .map(shiftTemplateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RosterAssignmentDto assignShiftToEmployee(RosterAssignmentDto rosterAssignmentDto) {
        Employee employee = employeeRepository.findById(rosterAssignmentDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        ShiftTemplate shiftTemplate = shiftTemplateRepository.findById(rosterAssignmentDto.getShiftTemplateId())
                .orElseThrow(() -> new ResourceNotFoundException("Shift template not found"));
        
        if (rosterAssignmentRepository.existsByEmployeeIdAndAssignmentDate(
                rosterAssignmentDto.getEmployeeId(), rosterAssignmentDto.getAssignmentDate())) {
            throw new DuplicateResourceException("Shift already assigned for this employee on the given date");
        }
        
        RosterAssignment rosterAssignment = rosterAssignmentMapper.toEntity(rosterAssignmentDto);
        rosterAssignment.setEmployee(employee);
        rosterAssignment.setShiftTemplate(shiftTemplate);
        
        RosterAssignment savedAssignment = rosterAssignmentRepository.save(rosterAssignment);
        return rosterAssignmentMapper.toDto(savedAssignment);
    }

    @Override
    public List<RosterAssignmentDto> getEmployeeSchedule(Long employeeId, LocalDate startDate, LocalDate endDate) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        return rosterAssignmentRepository.findByEmployeeAndAssignmentDateBetween(employee, startDate, endDate)
                .stream()
                .map(rosterAssignmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RosterAssignmentDto> getDepartmentSchedule(Long departmentId, LocalDate startDate, LocalDate endDate) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department not found");
        }
        
        return rosterAssignmentRepository.findByDepartmentAndDateRange(departmentId, startDate, endDate)
                .stream()
                .map(rosterAssignmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void validateAttendanceAgainstShift(Long employeeId, LocalDate date) {
        RosterAssignment assignment = rosterAssignmentRepository.findByEmployeeIdAndAssignmentDate(employeeId, date)
                .orElseThrow(() -> new ShiftException("No shift assigned for employee on this date"));
        
        if (assignment.isDayOff()) {
            return; // No validation needed for days off
        }
        
        Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, date)
                .orElseThrow(() -> new AttendanceException("No attendance record found"));
        
        LocalTime shiftStart = assignment.getShiftTemplate().getStartTime();
        LocalTime shiftEnd = assignment.getShiftTemplate().getEndTime();
        int gracePeriod = assignment.getShiftTemplate().getGracePeriodMinutes();
        
        // Check late arrival
        if (attendance.getCheckIn().toLocalTime().isAfter(shiftStart.plusMinutes(gracePeriod))) {
            attendance.setLate(true);
            attendanceRepository.save(attendance);
        }
        
        // Check early departure
        if (attendance.getCheckOut() != null && 
            attendance.getCheckOut().toLocalTime().isBefore(shiftEnd)) {
            attendance.setEarlyExit(true);
            attendanceRepository.save(attendance);
        }
    }
}