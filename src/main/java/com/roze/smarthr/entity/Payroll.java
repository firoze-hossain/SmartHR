
package com.roze.smarthr.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payrolls")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(nullable = false)
    private YearMonth payPeriod;
    
    private int workingDays;
    private int presentDays;
    private int paidLeaves;
    private int unpaidLeaves;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal grossSalary;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal lopAmount;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal netPayable;
    
    private boolean approved;
    private boolean finalized;
    
    @Column(nullable = false)
    private LocalDateTime generatedAt;
    
    private String remarks;
    
    @OneToOne(mappedBy = "payroll", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payslip payslip;
    
    @CreatedBy
    private String createdBy;
    
    @LastModifiedBy
    private String modifiedBy;
}