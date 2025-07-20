package com.roze.smarthr.dto;

import com.roze.smarthr.enums.OfferLetterStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OfferLetterResponse {
    private Long id;
    private Long candidateId;
    private String candidateName;
    private String offeredPosition;
    private LocalDate joiningDate;
    private BigDecimal salaryOffered;
    private OfferLetterStatus status;
    private Long issuedById;
    private String issuedByName;
    private LocalDate issueDate;
    private String documentUrl;
}