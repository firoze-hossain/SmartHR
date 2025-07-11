package com.roze.smarthr.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentRequest {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Location cannot be blank")
    private String location;
}