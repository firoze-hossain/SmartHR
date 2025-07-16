
package com.roze.smarthr.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkingDayRequest {
    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private boolean isWorking;
}