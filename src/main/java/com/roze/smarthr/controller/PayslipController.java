package com.roze.smarthr.controller;

import com.roze.smarthr.service.PayslipService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payslips")
@RequiredArgsConstructor
public class PayslipController {
    private final PayslipService payslipService;

    @GetMapping("/{payrollId}")
    @PreAuthorize("@securityService.hasAccessToPayslip(#payrollId)")
    public ResponseEntity<ByteArrayResource> downloadPayslip(@PathVariable Long payrollId) {
        byte[] pdf = payslipService.generatePayslipPdf(payrollId);

        ByteArrayResource resource = new ByteArrayResource(pdf);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=payslip-" + payrollId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}