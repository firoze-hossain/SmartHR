package com.roze.smarthr.service;

import com.roze.smarthr.dto.UserPreferenceRequest;
import com.roze.smarthr.dto.UserPreferenceResponse;
import com.roze.smarthr.entity.User;

public interface UserPreferenceService {
    UserPreferenceResponse createOrUpdateUserPreference(Long userId, UserPreferenceRequest request);

    UserPreferenceResponse getUserPreference(Long userId);

    String getUserLanguageCode(User user);

    String getUserDateFormat(User user);

    String getUserNumberFormat(User user);

    String getUserTimezone(User user);
}