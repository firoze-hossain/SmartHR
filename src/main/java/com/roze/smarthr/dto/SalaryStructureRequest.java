package com.roze.smarthr.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryStructureRequest {
    @NotNull
    private Long employeeId;
    
    @NotNull
    private BigDecimal basicSalary;
    
    private BigDecimal hra;
    private BigDecimal medicalAllowance;
    private BigDecimal transportAllowance;
    private BigDecimal otherAllowances;
    private BigDecimal deductions;
    
    @NotNull
    private LocalDate effectiveFrom;
}