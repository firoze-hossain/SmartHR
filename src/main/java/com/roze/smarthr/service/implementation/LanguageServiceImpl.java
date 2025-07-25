package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.LanguageRequest;
import com.roze.smarthr.dto.LanguageResponse;
import com.roze.smarthr.entity.Language;
import com.roze.smarthr.exception.DuplicateResourceException;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.LanguageMapper;
import com.roze.smarthr.repository.LanguageRepository;
import com.roze.smarthr.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    @Override
    @Transactional
    public LanguageResponse createLanguage(LanguageRequest request) {
        if (languageRepository.findByCode(request.getCode()).isPresent()) {
            throw new DuplicateResourceException("Language with code " + request.getCode() + " already exists");
        }

        Language language = languageMapper.toEntity(request);
        Language savedLanguage = languageRepository.save(language);

        if (savedLanguage.getIsDefault()) {
            setDefaultLanguage(savedLanguage.getId());
        }

        return languageMapper.toDto(savedLanguage);
    }

    @Override
    public LanguageResponse getLanguageById(Long id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + id));
        return languageMapper.toDto(language);
    }

    @Override
    public List<LanguageResponse> getAllLanguages() {
        return languageRepository.findAll().stream()
                .map(languageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LanguageResponse> getActiveLanguages() {
        return languageRepository.findByIsActiveTrue().stream()
                .map(languageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LanguageResponse updateLanguage(Long id, LanguageRequest request) {
        Language existingLanguage = languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + id));

        existingLanguage.setName(request.getName());
        existingLanguage.setIsActive(request.getIsActive());

        if (request.getIsDefault()) {
            setDefaultLanguage(id);
        } else if (existingLanguage.getIsDefault()) {
            throw new IllegalStateException("Cannot unset default language without setting another as default");
        }

        Language updatedLanguage = languageRepository.save(existingLanguage);
        return languageMapper.toDto(updatedLanguage);
    }

    //    @Override
//    @Transactional
//    public void setDefaultLanguage(Long id) {
//        // Unset current default
//        languageRepository.findByIsDefaultTrue().ifPresent(language -> {
//            language.setIsDefault(false);
//            languageRepository.save(language);
//        });
//
//        // Set new default
//        Language newDefault = languageRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + id));
//        newDefault.setIsDefault(true);
//        languageRepository.save(newDefault);
//    }
//
//    @Override
//    public LanguageResponse getDefaultLanguage() {
//        Language defaultLanguage = languageRepository.findByIsDefaultTrue()
//                .orElseThrow(() -> new ResourceNotFoundException("No default language set"));
//        return languageMapper.toDto(defaultLanguage);
//    }
    @Override
    @Transactional
    public void setDefaultLanguage(Long id) {
        // Get all current default languages
        List<Language> currentDefaults = languageRepository.findAllDefaultLanguages();

        // Unset all current defaults
        if (!currentDefaults.isEmpty()) {
            currentDefaults.forEach(lang -> {
                lang.setIsDefault(false);
                languageRepository.save(lang);
            });
        }

        // Set new default
        Language newDefault = languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with id: " + id));
        newDefault.setIsDefault(true);
        languageRepository.save(newDefault);
    }

    @Override
    public LanguageResponse getDefaultLanguage() {
        return languageRepository.findDefaultLanguage()
                .map(languageMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("No default language set"));
    }
}