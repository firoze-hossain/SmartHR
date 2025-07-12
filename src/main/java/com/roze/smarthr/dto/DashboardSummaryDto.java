package com.roze.smarthr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDto {
    private Long totalEmployees;
    private int onLeaveToday;
    private Long totalDepartments;
    private LeaveRequestCountDto leaveRequests;
}