package com.roze.smarthr.dto;

import com.roze.smarthr.enums.LeaveStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveStatusUpdateDto {
    @NotNull(message = "Status cannot be null")
    private LeaveStatus status;
}