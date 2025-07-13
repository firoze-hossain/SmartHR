package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.RosterAssignmentDto;
import com.roze.smarthr.dto.ShiftTemplateDto;
import com.roze.smarthr.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("shifts")
@RequiredArgsConstructor
public class ShiftController {
    private final ShiftService shiftService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/templates")
    public ResponseEntity<BaseResponse<ShiftTemplateDto>> createShiftTemplate(
            @Valid @RequestBody ShiftTemplateDto shiftTemplateDto) {
        ShiftTemplateDto response = shiftService.createShiftTemplate(shiftTemplateDto);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping("/templates")
    public ResponseEntity<BaseResponse<List<ShiftTemplateDto>>> getAllActiveShiftTemplates() {
        List<ShiftTemplateDto> response = shiftService.getAllActiveShiftTemplates();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping("/assignments")
    public ResponseEntity<BaseResponse<RosterAssignmentDto>> assignShiftToEmployee(
            @Valid @RequestBody RosterAssignmentDto rosterAssignmentDto) {
        RosterAssignmentDto response = shiftService.assignShiftToEmployee(rosterAssignmentDto);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Shift assigned successfully",
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR', 'MANAGER')")
    @GetMapping("/employees/{employeeId}/schedule")
    public ResponseEntity<BaseResponse<List<RosterAssignmentDto>>> getEmployeeSchedule(
            @PathVariable Long employeeId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<RosterAssignmentDto> response = shiftService.getEmployeeSchedule(employeeId, startDate, endDate);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR', 'MANAGER')")
    @GetMapping("/departments/{departmentId}/schedule")
    public ResponseEntity<BaseResponse<List<RosterAssignmentDto>>> getDepartmentSchedule(
            @PathVariable Long departmentId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        List<RosterAssignmentDto> response = shiftService.getDepartmentSchedule(departmentId, startDate, endDate);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR', 'MANAGER')")
    @PostMapping("/attendance_validation")
    public ResponseEntity<BaseResponse<Void>> validateAttendance(
            @RequestParam Long employeeId,
            @RequestParam LocalDate date) {
        shiftService.validateAttendanceAgainstShift(employeeId, date);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Attendance validated successfully",
                null
        ));
    }
}