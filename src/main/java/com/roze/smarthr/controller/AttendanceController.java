package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.AttendanceResponse;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendances")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping("/check_in")
    public ResponseEntity<BaseResponse<AttendanceResponse>> checkIn(@AuthenticationPrincipal User user) {
        AttendanceResponse response = attendanceService.checkIn(user);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Checked in successfully",
                response
        ));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping("/check_out")
    public ResponseEntity<BaseResponse<AttendanceResponse>> checkOut(@AuthenticationPrincipal User user) {
        AttendanceResponse response = attendanceService.checkOut(user);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Checked out successfully",
                response
        ));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping("/my")
    public ResponseEntity<BaseResponse<List<AttendanceResponse>>> getMyAttendance(@AuthenticationPrincipal User user) {
        List<AttendanceResponse> responses = attendanceService.getMyAttendance(user);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping("/{employeeId}")
    public ResponseEntity<BaseResponse<List<AttendanceResponse>>> getEmployeeAttendance(
            @PathVariable Long employeeId) {
        List<AttendanceResponse> responses = attendanceService.getEmployeeAttendance(employeeId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }
}