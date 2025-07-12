package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.DashboardSummaryDto;
import com.roze.smarthr.dto.DepartmentEmployeeCountDto;
import com.roze.smarthr.dto.LeaveRequestCountDto;
import com.roze.smarthr.dto.MonthlyLeaveCountDto;
import com.roze.smarthr.enums.LeaveStatus;
import com.roze.smarthr.repository.AttendanceRepository;
import com.roze.smarthr.repository.DepartmentRepository;
import com.roze.smarthr.repository.EmployeeRepository;
import com.roze.smarthr.repository.LeaveRequestRepository;
import com.roze.smarthr.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public DashboardSummaryDto getDashboardSummary() {
        return DashboardSummaryDto.builder()
                .totalEmployees(employeeRepository.count())
                .onLeaveToday(leaveRequestRepository.countEmployeesOnLeave(LocalDate.now()))
                .totalDepartments(departmentRepository.count())
                .leaveRequests(getLeaveRequestCounts())
                .build();
    }

    @Override
    public List<MonthlyLeaveCountDto> getMonthlyLeaveCounts() {
        return Arrays.stream(Month.values())
                .map(month -> {
                    String monthName = month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
                    int count = leaveRequestRepository.countByMonth(month.getValue());
                    return new MonthlyLeaveCountDto(monthName, count);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentEmployeeCountDto> getEmployeesPerDepartment() {
        return departmentRepository.countEmployeesByDepartment();
    }

    private LeaveRequestCountDto getLeaveRequestCounts() {
        return LeaveRequestCountDto.builder()
                .pending(leaveRequestRepository.countByStatus(LeaveStatus.PENDING))
                .approved(leaveRequestRepository.countByStatus(LeaveStatus.APPROVED))
                .rejected(leaveRequestRepository.countByStatus(LeaveStatus.REJECTED))
                .build();
    }
}