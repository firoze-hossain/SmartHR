// TrainingProgram.java
package com.roze.smarthr.entity;

import com.roze.smarthr.enums.TrainingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "training_programs")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String trainerName;

    @Column(nullable = false)
    private boolean mandatory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @OneToMany(mappedBy = "trainingProgram", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EmployeeTraining> employeeTrainings = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private LocalDate createdAt = LocalDate.now();

    @Column
    private LocalDate updatedAt;
}