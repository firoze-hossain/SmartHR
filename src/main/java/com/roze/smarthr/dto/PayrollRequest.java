package com.roze.smarthr.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.YearMonth;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollRequest {
    @NotNull
    private YearMonth month;
}

