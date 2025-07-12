package com.roze.smarthr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "leave_types")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., Sick, Casual, Annual

    @Column(nullable = false)
    private int annualQuota; // e.g., 12 days/year

    @Column(nullable = false)
    @Builder.Default
    private boolean carryForwardAllowed = false;

    @Builder.Default
    private int maxCarryForwardDays = 0;

}