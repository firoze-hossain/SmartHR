
package com.roze.smarthr.dto;


import com.roze.smarthr.enums.SettingType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalSettingResponse {
    private Long id;
    private String key;
    private String value;
    private String description;
    private SettingType type;
    private boolean editable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}