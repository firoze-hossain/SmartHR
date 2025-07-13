package com.roze.smarthr.service.implementation;

import com.roze.smarthr.entity.Payroll;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.PayrollRepository;
import com.roze.smarthr.service.PayslipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PayslipServiceImpl implements PayslipService {
    private final PayrollRepository payrollRepository;
    private final TemplateEngine templateEngine;

    @Override
    public byte[] generatePayslipPdf(Long payrollId) {
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found"));

        Context context = new Context();
        context.setVariable("payroll", payroll);
        context.setVariable("employee", payroll.getEmployee());
        context.setVariable("company", "SmartHR");
        context.setVariable("dateFormat", DateTimeFormatter.ofPattern("dd MMM yyyy"));

        String html = templateEngine.process("payslip", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate payslip", e);
        }
    }
}