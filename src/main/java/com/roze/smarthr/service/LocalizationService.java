package com.roze.smarthr.service;

import com.roze.smarthr.dto.LocalizedTextRequest;
import com.roze.smarthr.dto.LocalizedTextResponse;

import java.util.List;
import java.util.Map;

public interface LocalizationService {
    LocalizedTextResponse createTranslation(LocalizedTextRequest request);

    LocalizedTextResponse getTranslationById(Long id);

    List<LocalizedTextResponse> getTranslationsForEntity(String entityType, Long entityId);

    LocalizedTextResponse updateTranslation(Long id, LocalizedTextRequest request);

    void deleteTranslation(Long id);

    Map<String, String> getTranslationsForEntityAndLanguage(String entityType, Long entityId, String languageCode);

    String getTranslatedText(String entityType, Long entityId, String fieldName, String languageCode);
}