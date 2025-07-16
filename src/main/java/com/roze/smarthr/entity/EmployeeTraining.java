
package com.roze.smarthr.entity;

import com.roze.smarthr.enums.TrainingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "employee_trainings")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeTraining {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_program_id", nullable = false)
    private TrainingProgram trainingProgram;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingStatus status;

    @Column(nullable = false)
    private LocalDate enrolledDate;

    @Column
    private LocalDate completionDate;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column
    private Integer score;

    @Column
    private String certificateId;

    @Column
    private boolean feedbackSubmitted;
}