package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.DashboardSummaryDto;
import com.roze.smarthr.dto.DepartmentEmployeeCountDto;
import com.roze.smarthr.dto.MonthlyLeaveCountDto;
import com.roze.smarthr.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("dashboards")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping("/summary")
    public ResponseEntity<BaseResponse<DashboardSummaryDto>> getDashboardSummary() {
        DashboardSummaryDto response = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping("/leaves/monthly")
    public ResponseEntity<BaseResponse<List<MonthlyLeaveCountDto>>> getMonthlyLeaveCounts() {
        List<MonthlyLeaveCountDto> response = dashboardService.getMonthlyLeaveCounts();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping("/employees_per_department")
    public ResponseEntity<BaseResponse<List<DepartmentEmployeeCountDto>>> getEmployeesPerDepartment() {
        List<DepartmentEmployeeCountDto> response = dashboardService.getEmployeesPerDepartment();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }
}