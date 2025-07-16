
package com.roze.smarthr.dto;


import com.roze.smarthr.enums.HolidayType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HolidayResponse {
    private Long id;
    private String name;
    private LocalDate date;
    private HolidayType type;
    private String region;
    private boolean recurring;
    private LocalDateTime createdAt;
}