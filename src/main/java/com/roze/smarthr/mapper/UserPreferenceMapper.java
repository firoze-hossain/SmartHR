package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.UserPreferenceRequest;
import com.roze.smarthr.dto.UserPreferenceResponse;
import com.roze.smarthr.entity.UserPreference;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceMapper {
    public UserPreference toEntity(UserPreferenceRequest request) {
        return UserPreference.builder()
                .preferredLanguageCode(request.getPreferredLanguageCode())
                .timezone(request.getTimezone())
                .dateFormat(request.getDateFormat())
                .numberFormat(request.getNumberFormat())
                .build();
    }

    public UserPreferenceResponse toDto(UserPreference userPreference) {
        return UserPreferenceResponse.builder()
                .id(userPreference.getId())
                .userId(userPreference.getUser() != null ? userPreference.getUser().getId() : null)
                .preferredLanguageCode(userPreference.getPreferredLanguageCode())
                .timezone(userPreference.getTimezone())
                .dateFormat(userPreference.getDateFormat())
                .numberFormat(userPreference.getNumberFormat())
                .build();
    }
}