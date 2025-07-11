package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.DepartmentRequest;
import com.roze.smarthr.dto.DepartmentResponse;
import com.roze.smarthr.entity.Department;
import org.springframework.stereotype.Service;

@Service
public class DepartmentMapper {
    public Department toEntity(DepartmentRequest request) {
        return Department.builder()
                .title(request.getTitle())
                .location(request.getLocation())
                .build();
    }

    public DepartmentResponse toDto(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .title(department.getTitle())
                .location(department.getLocation())
                .employeeCount(department.getEmployees() != null ? department.getEmployees().size() : 0)
                .build();
    }
}