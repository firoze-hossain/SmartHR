package com.roze.smarthr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LanguageResponse {
    private Long id;
    private String code;
    private String name;
    private Boolean isDefault;
    private Boolean isActive;
}