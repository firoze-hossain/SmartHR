package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.FinalizePayrollRequest;
import com.roze.smarthr.dto.PayrollRequest;
import com.roze.smarthr.dto.PayrollResponse;
import com.roze.smarthr.service.PayrollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("payrolls")
@RequiredArgsConstructor
public class PayrollController {
    private final PayrollService payrollService;

    @PostMapping("/generate")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<BaseResponse<Void>> generatePayroll(
            @Valid @RequestBody PayrollRequest request) {
        payrollService.generateMonthlyPayroll(request.getMonth());
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Payroll generation initiated",
                null
        ));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<BaseResponse<List<PayrollResponse>>> getPayrolls(
            @RequestParam YearMonth month) {
        List<PayrollResponse> responses = payrollService.getPayrollsForMonth(month);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<BaseResponse<PayrollResponse>> getPayroll(@PathVariable Long id) {
        PayrollResponse response = payrollService.getPayroll(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PutMapping("/{id}/finalize")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    public ResponseEntity<BaseResponse<Void>> finalizePayroll(
            @PathVariable Long id,
            @Valid @RequestBody FinalizePayrollRequest request) {
        payrollService.finalizePayroll(id, request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Payroll finalized successfully",
                null
        ));
    }
}