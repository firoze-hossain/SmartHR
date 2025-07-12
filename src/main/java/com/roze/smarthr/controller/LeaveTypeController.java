package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.LeaveTypeDto;
import com.roze.smarthr.dto.LeaveTypeResponseDto;
import com.roze.smarthr.service.LeaveTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave-types")
@RequiredArgsConstructor
public class LeaveTypeController {
    private final LeaveTypeService leaveTypeService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponse<LeaveTypeResponseDto>> createLeaveType(
            @Valid @RequestBody LeaveTypeDto leaveTypeDto) {
        LeaveTypeResponseDto response = leaveTypeService.createLeaveType(leaveTypeDto);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<LeaveTypeResponseDto>>> getAllLeaveTypes() {
        List<LeaveTypeResponseDto> responses = leaveTypeService.getAllLeaveTypes();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<LeaveTypeResponseDto>> getLeaveTypeById(@PathVariable Long id) {
        LeaveTypeResponseDto response = leaveTypeService.getLeaveTypeById(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<LeaveTypeResponseDto>> updateLeaveType(
            @PathVariable Long id,
            @Valid @RequestBody LeaveTypeDto leaveTypeDto) {
        LeaveTypeResponseDto response = leaveTypeService.updateLeaveType(id, leaveTypeDto);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.UPDATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteLeaveType(@PathVariable Long id) {
        leaveTypeService.deleteLeaveType(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.DELETE_SUCCESS,
                null
        ));
    }
}