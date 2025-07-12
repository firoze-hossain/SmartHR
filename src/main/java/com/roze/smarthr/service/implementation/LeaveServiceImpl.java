package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.LeaveRequestDto;
import com.roze.smarthr.dto.LeaveResponseDto;
import com.roze.smarthr.dto.LeaveStatusUpdateDto;
import com.roze.smarthr.entity.*;
import com.roze.smarthr.enums.LeaveStatus;
import com.roze.smarthr.exception.LeaveException;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.LeaveMapper;
import com.roze.smarthr.repository.EmployeeLeaveBalanceRepository;
import com.roze.smarthr.repository.EmployeeRepository;
import com.roze.smarthr.repository.LeaveRequestRepository;
import com.roze.smarthr.repository.LeaveTypeRepository;
import com.roze.smarthr.service.EmployeeLeaveBalanceService;
import com.roze.smarthr.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveMapper leaveMapper;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeLeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeLeaveBalanceService leaveBalanceService;

    @Override
    public LeaveResponseDto applyForLeave(LeaveRequestDto request, User user) {
        validateLeaveDates(request);

        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found"));

        checkForOverlappingLeaves(employee, request.getFromDate(), request.getToDate());
        validateLeaveBalance(employee, leaveType, request.getFromDate(), request.getToDate());

        LeaveRequest leaveRequest = leaveMapper.toEntity(request, employee);
        leaveRequest.setLeaveType(leaveType);
        LeaveRequest savedLeave = leaveRequestRepository.save(leaveRequest);
        updateLeaveBalance(employee, leaveType, request.getFromDate(), request.getToDate());
        return leaveMapper.toDto(savedLeave);
    }

    @Override
    public List<LeaveResponseDto> getMyLeaves(User user) {
        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        return leaveRequestRepository.findByEmployeeOrderByFromDateDesc(employee)
                .stream()
                .map(leaveMapper::toDto)
                .toList();
    }

    @Override
    public List<LeaveResponseDto> getAllLeaves() {
        return leaveRequestRepository.findAllByOrderByFromDateDesc()
                .stream()
                .map(leaveMapper::toDto)
                .toList();
    }

    @Override
    public LeaveResponseDto updateLeaveStatus(Long leaveId, LeaveStatusUpdateDto statusUpdate) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Only pending leaves can be updated");
        }

        leaveRequest.setStatus(statusUpdate.getStatus());
        LeaveRequest updatedLeave = leaveRequestRepository.save(leaveRequest);
        return leaveMapper.toDto(updatedLeave);
    }

    private void validateLeaveDates(LeaveRequestDto request) {
        if (request.getFromDate().isAfter(request.getToDate())) {
            throw new IllegalArgumentException("From date must be before or equal to To date");
        }
    }

    private void checkForOverlappingLeaves(Employee employee, LocalDate fromDate, LocalDate toDate) {
        boolean hasOverlappingLeave = leaveRequestRepository.existsByEmployeeAndStatusAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                employee,
                LeaveStatus.APPROVED,
                toDate,
                fromDate);

        if (hasOverlappingLeave) {
            throw new IllegalArgumentException("You already have an approved leave during this period");
        }
    }

    private void validateLeaveBalance(Employee employee, LeaveType leaveType, LocalDate fromDate, LocalDate toDate) {
        int requestedDays = calculateWorkingDays(fromDate, toDate);
        EmployeeLeaveBalance balance = leaveBalanceRepository.findByEmployeeAndLeaveType(employee, leaveType)
                .orElseThrow(() -> new LeaveException("No leave balance found for this leave type"));

        if (balance.getAvailable() < requestedDays) {
            throw new LeaveException("Insufficient leave balance. Available: " + balance.getAvailable());
        }
    }

    private void updateLeaveBalance(Employee employee, LeaveType leaveType, LocalDate fromDate, LocalDate toDate) {
        int daysUsed = calculateWorkingDays(fromDate, toDate);
        leaveBalanceRepository.incrementUsedDays(employee.getId(), leaveType.getId(), daysUsed);
    }

    private int calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        // Simple implementation - for production use consider holidays and weekends
        return startDate.datesUntil(endDate.plusDays(1)).toList().size();
    }
}