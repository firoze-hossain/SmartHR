package com.roze.smarthr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPreferenceRequest {
    private String preferredLanguageCode;
    private String timezone;
    private String dateFormat;
    private String numberFormat;
}

