package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.LeaveRequestDto;
import com.roze.smarthr.dto.LeaveResponseDto;
import com.roze.smarthr.dto.LeaveStatusUpdateDto;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.service.LeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaves")
@RequiredArgsConstructor
public class LeaveController {
    private final LeaveService leaveService;

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<BaseResponse<LeaveResponseDto>> applyForLeave(
            @Valid @RequestBody LeaveRequestDto request,
            @AuthenticationPrincipal User user) {
        LeaveResponseDto response = leaveService.applyForLeave(request, user);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Leave application submitted successfully",
                response
        ));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping("/my")
    public ResponseEntity<BaseResponse<List<LeaveResponseDto>>> getMyLeaves(
            @AuthenticationPrincipal User user) {
        List<LeaveResponseDto> responses = leaveService.getMyLeaves(user);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping
    public ResponseEntity<BaseResponse<List<LeaveResponseDto>>> getAllLeaves() {
        List<LeaveResponseDto> responses = leaveService.getAllLeaves();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PutMapping("/{id}/status")
    public ResponseEntity<BaseResponse<LeaveResponseDto>> updateLeaveStatus(
            @PathVariable Long id,
            @Valid @RequestBody LeaveStatusUpdateDto statusUpdate) {
        LeaveResponseDto response = leaveService.updateLeaveStatus(id, statusUpdate);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Leave status updated successfully",
                response
        ));
    }
}