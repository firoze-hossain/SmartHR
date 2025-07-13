package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.ShiftTemplateDto;
import com.roze.smarthr.entity.ShiftTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShiftTemplateMapper {
    public ShiftTemplate toEntity(ShiftTemplateDto dto) {
        return ShiftTemplate.builder()
                .id(dto.getId())
                .name(dto.getName())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .gracePeriodMinutes(dto.getGracePeriodMinutes())
                .active(dto.isActive())
                .build();
    }

    public ShiftTemplateDto toDto(ShiftTemplate entity) {
        return ShiftTemplateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .gracePeriodMinutes(entity.getGracePeriodMinutes())
                .active(entity.isActive())
                .build();
    }
}