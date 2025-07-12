package com.roze.smarthr.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignLeaveBalancesRequest {
    @NotNull(message = "Employee ID cannot be null")
    private Long employeeId;
    
    @NotNull(message = "Leave type IDs cannot be null")
    private List<Long> leaveTypeIds;
}