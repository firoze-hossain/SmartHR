package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.EmployeeLeaveBalanceDto;
import com.roze.smarthr.dto.LeaveBalanceUpdateDto;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.service.EmployeeLeaveBalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave-balances")
@RequiredArgsConstructor
public class EmployeeLeaveBalanceController {
    private final EmployeeLeaveBalanceService leaveBalanceService;

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping("/my")
    public ResponseEntity<BaseResponse<List<EmployeeLeaveBalanceDto>>> getMyLeaveBalances(
            @AuthenticationPrincipal User user) {
        List<EmployeeLeaveBalanceDto> responses = leaveBalanceService.getEmployeeLeaveBalances(user);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PutMapping("/{employeeId}/{leaveTypeId}")
    public ResponseEntity<BaseResponse<EmployeeLeaveBalanceDto>> updateLeaveBalance(
            @PathVariable Long employeeId,
            @PathVariable Long leaveTypeId,
            @Valid @RequestBody LeaveBalanceUpdateDto updateDto) {
        EmployeeLeaveBalanceDto response = leaveBalanceService.updateLeaveBalance(
                employeeId, leaveTypeId, updateDto);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Leave balance updated successfully",
                response
        ));
    }
}