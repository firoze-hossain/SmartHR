
package com.roze.smarthr.dto;


import com.roze.smarthr.enums.SettingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalSettingRequest {
    @NotBlank
    private String key;

    @NotBlank
    private String value;

    private String description;

    @NotNull
    private SettingType type;

    @Builder.Default
    private boolean editable = true;
}