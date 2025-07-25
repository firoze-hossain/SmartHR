package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.UserPreferenceRequest;
import com.roze.smarthr.dto.UserPreferenceResponse;
import com.roze.smarthr.entity.Language;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.entity.UserPreference;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.UserPreferenceMapper;
import com.roze.smarthr.repository.LanguageRepository;
import com.roze.smarthr.repository.UserPreferenceRepository;
import com.roze.smarthr.repository.UserRepository;
import com.roze.smarthr.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserPreferenceServiceImpl implements UserPreferenceService {
    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;
    private final UserPreferenceMapper userPreferenceMapper;
    private final LanguageRepository languageRepository;

    @Override
    @Transactional
    public UserPreferenceResponse createOrUpdateUserPreference(Long userId, UserPreferenceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        UserPreference preference = userPreferenceRepository.findByUser(user)
                .orElseGet(() -> UserPreference.builder().user(user).build());

        preference.setPreferredLanguageCode(request.getPreferredLanguageCode());
        preference.setTimezone(request.getTimezone());
        preference.setDateFormat(request.getDateFormat());
        preference.setNumberFormat(request.getNumberFormat());

        UserPreference savedPreference = userPreferenceRepository.save(preference);
        return userPreferenceMapper.toDto(savedPreference);
    }

    @Override
    public UserPreferenceResponse getUserPreference(Long userId) {
        UserPreference preference = userPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User preference not found for user id: " + userId));
        return userPreferenceMapper.toDto(preference);
    }

    @Override
    public String getUserLanguageCode(User user) {
        return userPreferenceRepository.findByUser(user)
                .map(UserPreference::getPreferredLanguageCode)
                .orElseGet(() -> languageRepository.findByIsDefaultTrue()
                        .map(Language::getCode)
                        .orElse("en"));
    }

    @Override
    public String getUserDateFormat(User user) {
        return userPreferenceRepository.findByUser(user)
                .map(UserPreference::getDateFormat)
                .orElse("dd-MM-yyyy");
    }

    @Override
    public String getUserNumberFormat(User user) {
        return userPreferenceRepository.findByUser(user)
                .map(UserPreference::getNumberFormat)
                .orElse("#,###.##");
    }

    @Override
    public String getUserTimezone(User user) {
        return userPreferenceRepository.findByUser(user)
                .map(UserPreference::getTimezone)
                .orElse("UTC");
    }
}