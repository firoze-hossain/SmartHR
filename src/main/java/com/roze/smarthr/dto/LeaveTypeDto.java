package com.roze.smarthr.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveTypeDto {
    private Long id;
    
    @NotBlank(message = "Name cannot be blank")
    private String name;
    
    @NotNull(message = "Annual quota cannot be null")
    @Min(value = 1, message = "Annual quota must be at least 1")
    private Integer annualQuota;
    
    private Boolean carryForwardAllowed;
    
    @Min(value = 0, message = "Max carry forward days cannot be negative")
    private Integer maxCarryForwardDays;
}

