package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.EmployeeRequest;
import com.roze.smarthr.dto.EmployeeResponse;
import com.roze.smarthr.entity.Department;
import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.exception.DuplicateResourceException;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.EmployeeMapper;
import com.roze.smarthr.repository.DepartmentRepository;
import com.roze.smarthr.repository.EmployeeRepository;
import com.roze.smarthr.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByUserId(request.getUserId())) {
            throw new DuplicateResourceException("Employee already exists with user id: " + request.getUserId());
        }

        Employee employee = employeeMapper.toEntity(request);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(savedEmployee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        existingEmployee.setName(request.getName());
        existingEmployee.setJoiningDate(request.getJoiningDate());
        existingEmployee.setDesignation(request.getDesignation());
        if (!existingEmployee.getDepartment().getId().equals(request.getDepartmentId())) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));
            existingEmployee.setDepartment(department);
        }
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.toDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }
}