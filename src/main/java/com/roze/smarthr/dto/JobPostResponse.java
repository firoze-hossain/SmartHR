package com.roze.smarthr.dto;

import com.roze.smarthr.enums.EmploymentType;
import com.roze.smarthr.enums.JobPostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostResponse {
    private Long id;
    private String title;
    private Long departmentId;
    private String departmentName;
    private String location;
    private EmploymentType employmentType;
    private String jobDescription;
    private String requirements;
    private JobPostStatus status;
    private LocalDate postedDate;
    private LocalDate closingDate;
    private Long postedById;
    private String postedByName;
}