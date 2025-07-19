package com.roze.smarthr.dto;

import com.roze.smarthr.enums.EmploymentType;
import com.roze.smarthr.enums.JobPostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostRequest {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Department ID cannot be null")
    private Long departmentId;

    @NotBlank(message = "Location cannot be blank")
    private String location;

    @NotNull(message = "Employment type cannot be null")
    private EmploymentType employmentType;

    @NotBlank(message = "Job description cannot be blank")
    private String jobDescription;

    @NotBlank(message = "Requirements cannot be blank")
    private String requirements;

    @NotNull(message = "Closing date cannot be null")
    private LocalDate closingDate;
}

