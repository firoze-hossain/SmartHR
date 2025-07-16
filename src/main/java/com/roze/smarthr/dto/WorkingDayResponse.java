
package com.roze.smarthr.dto;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkingDayResponse {
    private Long id;
    private DayOfWeek dayOfWeek;
    private boolean isWorking;
    private LocalDateTime updatedAt;
}