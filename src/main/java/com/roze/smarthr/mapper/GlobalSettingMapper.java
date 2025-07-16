
package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.GlobalSettingRequest;
import com.roze.smarthr.dto.GlobalSettingResponse;
import com.roze.smarthr.entity.GlobalSetting;
import org.springframework.stereotype.Component;

@Component
public class GlobalSettingMapper {
    public GlobalSetting toEntity(GlobalSettingRequest request) {
        return GlobalSetting.builder()
                .key(request.getKey())
                .value(request.getValue())
                .description(request.getDescription())
                .type(request.getType())
                .editable(request.isEditable())
                .build();
    }

    public GlobalSettingResponse toDto(GlobalSetting setting) {
        return GlobalSettingResponse.builder()
                .id(setting.getId())
                .key(setting.getKey())
                .value(setting.getValue())
                .description(setting.getDescription())
                .type(setting.getType())
                .editable(setting.isEditable())
                .createdAt(setting.getCreatedAt())
                .updatedAt(setting.getUpdatedAt())
                .build();
    }
}