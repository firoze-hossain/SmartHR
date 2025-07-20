package com.roze.smarthr.entity;

import com.roze.smarthr.enums.OfferLetterStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "offer_letters")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column(nullable = false)
    private String offeredPosition;

    @Column(nullable = false)
    private LocalDate joiningDate;

    @Column(nullable = false)
    private BigDecimal salaryOffered;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfferLetterStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by_id", nullable = false)
    private User issuedBy;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private String documentUrl;

    @Column(columnDefinition = "TEXT")
    private String termsAndConditions;
}