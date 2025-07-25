package com.roze.smarthr.service;

import com.roze.smarthr.dto.LanguageRequest;
import com.roze.smarthr.dto.LanguageResponse;

import java.util.List;

public interface LanguageService {
    LanguageResponse createLanguage(LanguageRequest request);

    LanguageResponse getLanguageById(Long id);

    List<LanguageResponse> getAllLanguages();

    List<LanguageResponse> getActiveLanguages();

    LanguageResponse updateLanguage(Long id, LanguageRequest request);

    void setDefaultLanguage(Long id);

    LanguageResponse getDefaultLanguage();
}