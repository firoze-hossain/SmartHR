// SecurityService.java
package com.roze.smarthr.service;

import com.roze.smarthr.entity.Payroll;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.repository.PayrollRepository;
import com.roze.smarthr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final PayrollRepository payrollRepository;
    private final UserRepository userRepository;

    public boolean hasAccessToPayslip(Long payrollId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new RuntimeException("Payroll not found"));

        // Allow HR/Admin or the employee themselves
        return user.getRoles().stream().anyMatch(role ->
                role.getTitle().equals("HR") || role.getTitle().equals("ADMIN"))
                || payroll.getEmployee().getUser().getId().equals(user.getId());
    }
}