package com.roze.smarthr.service;

import com.roze.smarthr.dto.AssignLeaveBalancesRequest;
import com.roze.smarthr.dto.EmployeeRequest;
import com.roze.smarthr.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);

    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse getEmployeeById(Long id);

    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);

    void deleteEmployee(Long id);

    EmployeeResponse assignLeaveBalances(AssignLeaveBalancesRequest request);
}