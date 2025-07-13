package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.SalaryStructureRequest;
import com.roze.smarthr.dto.SalaryStructureResponse;
import com.roze.smarthr.service.SalaryStructureService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("salary_structures")
@RequiredArgsConstructor
public class SalaryStructureController {
    private final SalaryStructureService salaryStructureService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<BaseResponse<SalaryStructureResponse>> createSalaryStructure(
            @Valid @RequestBody SalaryStructureRequest request) {
        SalaryStructureResponse response = salaryStructureService.createSalaryStructure(request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<BaseResponse<List<SalaryStructureResponse>>> getCurrentSalaryStructures() {
        List<SalaryStructureResponse> responses = salaryStructureService.getCurrentSalaryStructures();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<BaseResponse<SalaryStructureResponse>> getSalaryStructure(@PathVariable Long id) {
        SalaryStructureResponse response = salaryStructureService.getSalaryStructure(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<BaseResponse<SalaryStructureResponse>> updateSalaryStructure(
            @PathVariable Long id,
            @Valid @RequestBody SalaryStructureRequest request) {
        SalaryStructureResponse response = salaryStructureService.updateSalaryStructure(id, request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.UPDATE_SUCCESS,
                response
        ));
    }
}