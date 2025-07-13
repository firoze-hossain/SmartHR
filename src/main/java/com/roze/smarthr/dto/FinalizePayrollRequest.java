package com.roze.smarthr.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinalizePayrollRequest {
    @NotNull
    private String remarks;
}