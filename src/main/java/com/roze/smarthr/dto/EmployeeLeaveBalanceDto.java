package com.roze.smarthr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeLeaveBalanceDto {
    private Long leaveTypeId;
    private String leaveTypeName;
    private Integer totalQuota;
    private Integer used;
    private Integer carriedForward;
   // private Integer available;
   public Integer getAvailable() {
       return totalQuota - used + carriedForward;
   }
}

