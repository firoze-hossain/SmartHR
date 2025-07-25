package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.LocalizedTextRequest;
import com.roze.smarthr.dto.LocalizedTextResponse;
import com.roze.smarthr.entity.LocalizedText;
import org.springframework.stereotype.Service;

@Service
public class LocalizedTextMapper {
    public LocalizedText toEntity(LocalizedTextRequest request) {
        return LocalizedText.builder()
                .entityType(request.getEntityType())
                .entityId(request.getEntityId())
                .fieldName(request.getFieldName())
                .languageCode(request.getLanguageCode())
                .translatedText(request.getTranslatedText())
                .build();
    }

    public LocalizedTextResponse toDto(LocalizedText localizedText) {
        return LocalizedTextResponse.builder()
                .id(localizedText.getId())
                .entityType(localizedText.getEntityType())
                .entityId(localizedText.getEntityId())
                .fieldName(localizedText.getFieldName())
                .languageCode(localizedText.getLanguageCode())
                .translatedText(localizedText.getTranslatedText())
                .build();
    }
}