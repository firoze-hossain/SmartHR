package com.roze.smarthr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "employee_leave_balances")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeLeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private int totalQuota;

    @Column(nullable = false)
    @Builder.Default
    private int used = 0;

    @Column(nullable = false)
    @Builder.Default
    private int carriedForward = 0;

    @Column(nullable = false)
    @Builder.Default
    private int available = 0; // derived: totalQuota - used + carriedForward
}