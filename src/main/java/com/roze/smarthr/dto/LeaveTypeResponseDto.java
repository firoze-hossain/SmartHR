package com.roze.smarthr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveTypeResponseDto {
    private Long id;
    private String name;
    private Integer annualQuota;
    private Boolean carryForwardAllowed;
    private Integer maxCarryForwardDays;
}