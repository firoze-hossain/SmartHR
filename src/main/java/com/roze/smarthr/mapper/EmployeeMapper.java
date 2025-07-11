package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.EmployeeRequest;
import com.roze.smarthr.dto.EmployeeResponse;
import com.roze.smarthr.entity.Department;
import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.DepartmentRepository;
import com.roze.smarthr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeMapper {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public Employee toEntity(EmployeeRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        return Employee.builder()
                .name(request.getName())
                .joiningDate(request.getJoiningDate())
                .designation(request.getDesignation())
                .department(department)
                .user(user)
                .build();
    }

    public EmployeeResponse toDto(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .joiningDate(employee.getJoiningDate())
                .designation(employee.getDesignation())
                .departmentId(employee.getDepartment().getId())
                .departmentName(employee.getDepartment().getTitle())
                .userId(employee.getUser().getId())
                .userEmail(employee.getUser().getEmail())
                .build();
    }
}