package com.roze.smarthr.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveRequestDto {
    @NotNull(message = "From date cannot be null")
    @FutureOrPresent(message = "From date must be today or in the future")
    private LocalDate fromDate;

    @NotNull(message = "To date cannot be null")
    @FutureOrPresent(message = "To date must be today or in the future")
    private LocalDate toDate;

    @NotBlank(message = "Reason cannot be blank")
    private String reason;

    @NotNull(message = "Leave type ID cannot be null")
    private Long leaveTypeId;
}