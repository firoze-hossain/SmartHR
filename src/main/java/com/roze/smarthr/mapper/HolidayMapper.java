
package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.HolidayRequest;
import com.roze.smarthr.dto.HolidayResponse;
import com.roze.smarthr.entity.Holiday;
import org.springframework.stereotype.Component;

@Component
public class HolidayMapper {
    public Holiday toEntity(HolidayRequest request) {
        return Holiday.builder()
                .name(request.getName())
                .date(request.getDate())
                .type(request.getType())
                .region(request.getRegion())
                .recurring(request.isRecurring())
                .build();
    }

    public HolidayResponse toDto(Holiday holiday) {
        return HolidayResponse.builder()
                .id(holiday.getId())
                .name(holiday.getName())
                .date(holiday.getDate())
                .type(holiday.getType())
                .region(holiday.getRegion())
                .recurring(holiday.isRecurring())
                .createdAt(holiday.getCreatedAt())
                .build();
    }
}