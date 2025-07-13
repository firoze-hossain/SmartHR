package com.roze.smarthr.service;

import com.roze.smarthr.dto.SalaryStructureRequest;
import com.roze.smarthr.dto.SalaryStructureResponse;

import java.util.List;

public interface SalaryStructureService {
    SalaryStructureResponse createSalaryStructure(SalaryStructureRequest request);

    SalaryStructureResponse getSalaryStructure(Long id);

    List<SalaryStructureResponse> getCurrentSalaryStructures();

    SalaryStructureResponse updateSalaryStructure(Long id, SalaryStructureRequest request);
}