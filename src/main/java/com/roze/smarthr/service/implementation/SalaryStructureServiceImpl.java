package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.SalaryStructureRequest;
import com.roze.smarthr.dto.SalaryStructureResponse;
import com.roze.smarthr.entity.SalaryStructure;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.SalaryStructureMapper;
import com.roze.smarthr.repository.SalaryStructureRepository;
import com.roze.smarthr.service.SalaryStructureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryStructureServiceImpl implements SalaryStructureService {
    private final SalaryStructureRepository salaryStructureRepository;
    private final SalaryStructureMapper mapper;

    @Override
    @Transactional
    public SalaryStructureResponse createSalaryStructure(SalaryStructureRequest request) {
        // End previous structure if exists
        salaryStructureRepository.findByEmployeeId(request.getEmployeeId())
                .ifPresent(structure -> {
                    structure.setEffectiveTo(LocalDate.now().minusDays(1));
                    salaryStructureRepository.save(structure);
                });

        SalaryStructure newStructure = mapper.toEntity(request);
        SalaryStructure saved = salaryStructureRepository.save(newStructure);
        return mapper.toDto(saved);
    }

    @Override
    public SalaryStructureResponse getSalaryStructure(Long id) {
        SalaryStructure structure = salaryStructureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary structure not found"));
        return mapper.toDto(structure);
    }

    @Override
    public List<SalaryStructureResponse> getCurrentSalaryStructures() {
        return salaryStructureRepository.findByEffectiveToIsNull().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SalaryStructureResponse updateSalaryStructure(Long id, SalaryStructureRequest request) {
        SalaryStructure existing = salaryStructureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Salary structure not found"));
        
        // Create new version instead of updating
        return createSalaryStructure(request);
    }
}