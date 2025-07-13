package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.SalaryStructureRequest;
import com.roze.smarthr.dto.SalaryStructureResponse;
import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.SalaryStructure;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaryStructureMapper {
    private final EmployeeRepository employeeRepository;

    public SalaryStructure toEntity(SalaryStructureRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        
        return SalaryStructure.builder()
                .employee(employee)
                .basicSalary(request.getBasicSalary())
                .hra(request.getHra())
                .medicalAllowance(request.getMedicalAllowance())
                .transportAllowance(request.getTransportAllowance())
                .otherAllowances(request.getOtherAllowances())
                .deductions(request.getDeductions())
                .effectiveFrom(request.getEffectiveFrom())
                .build();
    }

    public SalaryStructureResponse toDto(SalaryStructure salaryStructure) {
        return SalaryStructureResponse.builder()
                .id(salaryStructure.getId())
                .employeeId(salaryStructure.getEmployee().getId())
                .employeeName(salaryStructure.getEmployee().getName())
                .basicSalary(salaryStructure.getBasicSalary())
                .hra(salaryStructure.getHra())
                .medicalAllowance(salaryStructure.getMedicalAllowance())
                .transportAllowance(salaryStructure.getTransportAllowance())
                .otherAllowances(salaryStructure.getOtherAllowances())
                .deductions(salaryStructure.getDeductions())
                .grossSalary(salaryStructure.getGrossSalary())
                .effectiveFrom(salaryStructure.getEffectiveFrom())
                .effectiveTo(salaryStructure.getEffectiveTo())
                .build();
    }
}