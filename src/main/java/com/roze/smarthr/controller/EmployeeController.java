package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.EmployeeRequest;
import com.roze.smarthr.dto.EmployeeResponse;
import com.roze.smarthr.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping
    public ResponseEntity<BaseResponse<EmployeeResponse>> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<EmployeeResponse>>> getAllEmployees() {
        List<EmployeeResponse> responses = employeeService.getAllEmployees();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<EmployeeResponse>> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<EmployeeResponse>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.UPDATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.DELETE_SUCCESS,
                null
        ));
    }
}