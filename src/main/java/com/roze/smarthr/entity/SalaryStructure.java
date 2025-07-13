// SalaryStructure.java
package com.roze.smarthr.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "salary_structures")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basicSalary;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal hra;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal medicalAllowance;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal transportAllowance;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal otherAllowances;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal deductions;
    
    @Column(nullable = false)
    private LocalDate effectiveFrom;
    
    @Column
    private LocalDate effectiveTo;
    
    public BigDecimal getGrossSalary() {
        return basicSalary
            .add(hra != null ? hra : BigDecimal.ZERO)
            .add(medicalAllowance != null ? medicalAllowance : BigDecimal.ZERO)
            .add(transportAllowance != null ? transportAllowance : BigDecimal.ZERO)
            .add(otherAllowances != null ? otherAllowances : BigDecimal.ZERO);
    }
}



