package com.roze.smarthr.service;

public interface PayslipService {
    byte[] generatePayslipPdf(Long payrollId);
}