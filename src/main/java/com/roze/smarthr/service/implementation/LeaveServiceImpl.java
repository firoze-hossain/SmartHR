package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.LeaveRequestDto;
import com.roze.smarthr.dto.LeaveResponseDto;
import com.roze.smarthr.dto.LeaveStatusUpdateDto;
import com.roze.smarthr.entity.Employee;
import com.roze.smarthr.entity.LeaveRequest;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.enums.LeaveStatus;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.LeaveMapper;
import com.roze.smarthr.repository.EmployeeRepository;
import com.roze.smarthr.repository.LeaveRequestRepository;
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

    @Override
    public LeaveResponseDto applyForLeave(LeaveRequestDto request, User user) {
        validateLeaveDates(request);

        Employee employee = employeeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        checkForOverlappingLeaves(employee, request.getFromDate(), request.getToDate());

        LeaveRequest leaveRequest = leaveMapper.toEntity(request, employee);
        LeaveRequest savedLeave = leaveRequestRepository.save(leaveRequest);
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
}