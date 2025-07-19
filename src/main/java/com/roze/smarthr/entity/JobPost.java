package com.roze.smarthr.entity;

import com.roze.smarthr.enums.EmploymentType;
import com.roze.smarthr.enums.JobPostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "job_posts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentType employmentType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String jobDescription;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String requirements;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobPostStatus status;

    @Column(nullable = false)
    private LocalDate postedDate;

    @Column(nullable = false)
    private LocalDate closingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by_id", nullable = false)
    private User postedBy;
}