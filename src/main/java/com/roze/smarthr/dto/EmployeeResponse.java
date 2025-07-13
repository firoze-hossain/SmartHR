package com.roze.smarthr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponse {
    private Long id;
    private String name;
    private LocalDate joiningDate;
    private LocalDate birthDate;
    private String designation;
    private Long departmentId;
    private String departmentName;
    private Long userId;
    private String userEmail;
}