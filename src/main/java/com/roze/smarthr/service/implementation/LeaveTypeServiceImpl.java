package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.LeaveTypeDto;
import com.roze.smarthr.dto.LeaveTypeResponseDto;
import com.roze.smarthr.entity.LeaveType;
import com.roze.smarthr.exception.DuplicateResourceException;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.LeaveTypeMapper;
import com.roze.smarthr.repository.EmployeeLeaveBalanceRepository;
import com.roze.smarthr.repository.LeaveTypeRepository;
import com.roze.smarthr.service.LeaveTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveTypeServiceImpl implements LeaveTypeService {
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveTypeMapper leaveTypeMapper;
    private final EmployeeLeaveBalanceRepository leaveBalanceRepository;

    @Override
    public LeaveTypeResponseDto createLeaveType(LeaveTypeDto leaveTypeDto) {
        // Check if leave type with same name already exists
        if (leaveTypeRepository.existsByNameIgnoreCase(leaveTypeDto.getName())) {
            throw new DuplicateResourceException("Leave type with name '" + leaveTypeDto.getName() + "' already exists");
        }

        LeaveType leaveType = leaveTypeMapper.toEntity(leaveTypeDto);
        LeaveType savedLeaveType = leaveTypeRepository.save(leaveType);
        return leaveTypeMapper.toDto(savedLeaveType);
    }

    @Override
    public List<LeaveTypeResponseDto> getAllLeaveTypes() {
        return leaveTypeRepository.findAll()
                .stream()
                .map(leaveTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveTypeResponseDto getLeaveTypeById(Long id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + id));
        return leaveTypeMapper.toDto(leaveType);
    }

    @Override
    public LeaveTypeResponseDto updateLeaveType(Long id, LeaveTypeDto leaveTypeDto) {
        LeaveType existingLeaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + id));

        // Check if name is being changed to an existing one
        if (!existingLeaveType.getName().equalsIgnoreCase(leaveTypeDto.getName())) {
            if (leaveTypeRepository.existsByNameIgnoreCase(leaveTypeDto.getName())) {
                throw new DuplicateResourceException("Leave type with name '" + leaveTypeDto.getName() + "' already exists");
            }
        }

        // Update fields
        existingLeaveType.setName(leaveTypeDto.getName());
        existingLeaveType.setAnnualQuota(leaveTypeDto.getAnnualQuota());
        existingLeaveType.setCarryForwardAllowed(leaveTypeDto.getCarryForwardAllowed());
        existingLeaveType.setMaxCarryForwardDays(leaveTypeDto.getMaxCarryForwardDays());

        LeaveType updatedLeaveType = leaveTypeRepository.save(existingLeaveType);
        return leaveTypeMapper.toDto(updatedLeaveType);
    }

    @Override
    public void deleteLeaveType(Long id) {
        LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave type not found with id: " + id));

        // Check if any employee has balance for this leave type using repository
        if (leaveBalanceRepository.existsByLeaveType(leaveType)) {
            throw new IllegalStateException("Cannot delete leave type with existing employee balances");
        }

        leaveTypeRepository.delete(leaveType);
    }
}