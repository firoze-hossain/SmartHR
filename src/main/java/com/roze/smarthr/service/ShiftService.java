package com.roze.smarthr.service;

import com.roze.smarthr.dto.RosterAssignmentDto;
import com.roze.smarthr.dto.ShiftTemplateDto;

import java.time.LocalDate;
import java.util.List;

public interface ShiftService {
    ShiftTemplateDto createShiftTemplate(ShiftTemplateDto shiftTemplateDto);

    List<ShiftTemplateDto> getAllActiveShiftTemplates();

    RosterAssignmentDto assignShiftToEmployee(RosterAssignmentDto rosterAssignmentDto);

    List<RosterAssignmentDto> getEmployeeSchedule(Long employeeId, LocalDate startDate, LocalDate endDate);

    List<RosterAssignmentDto> getDepartmentSchedule(Long departmentId, LocalDate startDate, LocalDate endDate);

    void validateAttendanceAgainstShift(Long employeeId, LocalDate date);
}