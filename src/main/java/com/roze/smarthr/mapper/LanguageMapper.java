package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.LanguageRequest;
import com.roze.smarthr.dto.LanguageResponse;
import com.roze.smarthr.entity.Language;
import org.springframework.stereotype.Service;

@Service
public class LanguageMapper {
    public Language toEntity(LanguageRequest request) {
        return Language.builder()
                .code(request.getCode())
                .name(request.getName())
                .isDefault(request.getIsDefault())
                .isActive(request.getIsActive())
                .build();
    }

    public LanguageResponse toDto(Language language) {
        return LanguageResponse.builder()
                .id(language.getId())
                .code(language.getCode())
                .name(language.getName())
                .isDefault(language.getIsDefault())
                .isActive(language.getIsActive())
                .build();
    }
}