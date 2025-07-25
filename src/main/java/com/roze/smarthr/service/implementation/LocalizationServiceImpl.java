package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.LocalizedTextRequest;
import com.roze.smarthr.dto.LocalizedTextResponse;
import com.roze.smarthr.entity.Language;
import com.roze.smarthr.entity.LocalizedText;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.LocalizedTextMapper;
import com.roze.smarthr.repository.LanguageRepository;
import com.roze.smarthr.repository.LocalizedTextRepository;
import com.roze.smarthr.service.LocalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalizationServiceImpl implements LocalizationService {
    private final LocalizedTextRepository localizedTextRepository;
    private final LanguageRepository languageRepository;
    private final LocalizedTextMapper localizedTextMapper;

    @Override
    @Transactional
    public LocalizedTextResponse createTranslation(LocalizedTextRequest request) {
        // Verify language exists
        languageRepository.findByCode(request.getLanguageCode())
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code: " + request.getLanguageCode()));

        LocalizedText translation = localizedTextMapper.toEntity(request);
        LocalizedText savedTranslation = localizedTextRepository.save(translation);
        return localizedTextMapper.toDto(savedTranslation);
    }

    @Override
    public LocalizedTextResponse getTranslationById(Long id) {
        LocalizedText translation = localizedTextRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Translation not found with id: " + id));
        return localizedTextMapper.toDto(translation);
    }

    @Override
    public List<LocalizedTextResponse> getTranslationsForEntity(String entityType, Long entityId) {
        return localizedTextRepository.findAllTranslationsForEntity(entityType, entityId).stream()
                .map(localizedTextMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LocalizedTextResponse updateTranslation(Long id, LocalizedTextRequest request) {
        LocalizedText existingTranslation = localizedTextRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Translation not found with id: " + id));

        existingTranslation.setTranslatedText(request.getTranslatedText());
        LocalizedText updatedTranslation = localizedTextRepository.save(existingTranslation);
        return localizedTextMapper.toDto(updatedTranslation);
    }

    @Override
    @Transactional
    public void deleteTranslation(Long id) {
        if (!localizedTextRepository.existsById(id)) {
            throw new ResourceNotFoundException("Translation not found with id: " + id);
        }
        localizedTextRepository.deleteById(id);
    }

    @Override
    public Map<String, String> getTranslationsForEntityAndLanguage(String entityType, Long entityId, String languageCode) {
        return localizedTextRepository.findByEntityTypeAndEntityIdAndLanguageCode(entityType, entityId, languageCode)
                .stream()
                .collect(Collectors.toMap(
                        LocalizedText::getFieldName,
                        LocalizedText::getTranslatedText
                ));
    }

    @Override
    public String getTranslatedText(String entityType, Long entityId, String fieldName, String languageCode) {
        return localizedTextRepository
                .findByEntityTypeAndEntityIdAndFieldNameAndLanguageCode(entityType, entityId, fieldName, languageCode)
                .map(LocalizedText::getTranslatedText)
                .orElse(null);
    }
}