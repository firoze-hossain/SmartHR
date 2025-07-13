// PayrollResponse.java
package com.roze.smarthr.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollResponse {
    private Long id;
    private Long employeeId;
    private String employeeName;
    private YearMonth payPeriod;
    private int workingDays;
    private int presentDays;
    private int paidLeaves;
    private int unpaidLeaves;
    private BigDecimal grossSalary;
    private BigDecimal lopAmount;
    private BigDecimal netPayable;
    private boolean approved;
    private boolean finalized;
    private LocalDateTime generatedAt;
    private String remarks;
    private String payslipUrl;
}