package com.roze.smarthr.service;


import com.roze.smarthr.dto.DashboardSummaryDto;
import com.roze.smarthr.dto.DepartmentEmployeeCountDto;
import com.roze.smarthr.dto.MonthlyLeaveCountDto;

import java.util.List;

public interface DashboardService {
    DashboardSummaryDto getDashboardSummary();

    List<MonthlyLeaveCountDto> getMonthlyLeaveCounts();

    List<DepartmentEmployeeCountDto> getEmployeesPerDepartment();
}