package com.roze.smarthr.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RosterAssignmentDto {
    private Long id;
    
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    
    @NotNull(message = "Shift template ID is required")
    private Long shiftTemplateId;
    
    @NotNull(message = "Assignment date is required")
    private LocalDate assignmentDate;
    
    private boolean isDayOff;
}