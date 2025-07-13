package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.RosterAssignmentDto;
import com.roze.smarthr.entity.RosterAssignment;
import com.roze.smarthr.repository.EmployeeRepository;
import com.roze.smarthr.repository.ShiftTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RosterAssignmentMapper {
    private final EmployeeRepository employeeRepository;
    private final ShiftTemplateRepository shiftTemplateRepository;

    public RosterAssignment toEntity(RosterAssignmentDto dto) {
        return RosterAssignment.builder()
                .id(dto.getId())
                .employee(employeeRepository.getReferenceById(dto.getEmployeeId()))
                .shiftTemplate(shiftTemplateRepository.getReferenceById(dto.getShiftTemplateId()))
                .assignmentDate(dto.getAssignmentDate())
                .isDayOff(dto.isDayOff())
                .build();
    }

    public RosterAssignmentDto toDto(RosterAssignment entity) {
        return RosterAssignmentDto.builder()
                .id(entity.getId())
                .employeeId(entity.getEmployee().getId())
                .shiftTemplateId(entity.getShiftTemplate().getId())
                .assignmentDate(entity.getAssignmentDate())
                .isDayOff(entity.isDayOff())
                .build();
    }
}