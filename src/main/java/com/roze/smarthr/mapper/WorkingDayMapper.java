
package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.WorkingDayResponse;
import com.roze.smarthr.entity.WorkingDay;
import org.springframework.stereotype.Component;

@Component
public class WorkingDayMapper {
    public WorkingDayResponse toDto(WorkingDay workingDay) {
        return WorkingDayResponse.builder()
                .id(workingDay.getId())
                .dayOfWeek(workingDay.getDayOfWeek())
                .isWorking(workingDay.isWorking())
                .updatedAt(workingDay.getUpdatedAt())
                .build();
    }
}