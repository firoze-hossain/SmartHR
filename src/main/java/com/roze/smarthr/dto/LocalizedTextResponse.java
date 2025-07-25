package com.roze.smarthr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocalizedTextResponse {
    private Long id;
    private String entityType;
    private Long entityId;
    private String fieldName;
    private String languageCode;
    private String translatedText;
}