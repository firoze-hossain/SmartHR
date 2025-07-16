
package com.roze.smarthr.dto;

import com.roze.smarthr.enums.HolidayType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HolidayRequest {
    @NotBlank
    private String name;

    @NotNull
    private LocalDate date;

    @NotNull
    private HolidayType type;

    private String region;

    @Builder.Default
    private boolean recurring = false;
}