package com.roze.smarthr.dto;

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
public class EmployeeRequest {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    
    @NotNull(message = "Joining date cannot be null")
    private LocalDate joiningDate;
    
    @NotBlank(message = "Designation cannot be blank")
    private String designation;
    
    @NotNull(message = "Department ID cannot be null")
    private Long departmentId;
    
    @NotNull(message = "User ID cannot be null")
    private Long userId;
}
