package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.DepartmentRequest;
import com.roze.smarthr.dto.DepartmentResponse;
import com.roze.smarthr.entity.Department;
import com.roze.smarthr.exception.DuplicateResourceException;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.DepartmentMapper;
import com.roze.smarthr.repository.DepartmentRepository;
import com.roze.smarthr.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByTitleIgnoreCase(request.getTitle())) {
            throw new DuplicateResourceException("Department with title '" + request.getTitle() + "' already exists");
        }

        Department department = departmentMapper.toEntity(request);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDto(savedDepartment);
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return departmentMapper.toDto(department);
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        if (!existingDepartment.getTitle().equalsIgnoreCase(request.getTitle())) {
            if (departmentRepository.existsByTitleIgnoreCase(request.getTitle())) {
                throw new DuplicateResourceException("Department with title '" + request.getTitle() + "' already exists");
            }
        }

        existingDepartment.setTitle(request.getTitle());
        existingDepartment.setLocation(request.getLocation());

        Department updatedDepartment = departmentRepository.save(existingDepartment);
        return departmentMapper.toDto(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));

        if (!department.getEmployees().isEmpty()) {
            throw new IllegalStateException("Cannot delete department with assigned employees");
        }

        departmentRepository.delete(department);
    }
}