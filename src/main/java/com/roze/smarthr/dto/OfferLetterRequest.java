package com.roze.smarthr.dto;

import com.roze.smarthr.enums.OfferLetterStatus;
import jakarta.validation.constraints.*;
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
public class OfferLetterRequest {
    @NotNull(message = "Candidate ID cannot be null")
    private Long candidateId;

    @NotBlank(message = "Offered position cannot be blank")
    private String offeredPosition;

    @Future(message = "Joining date must be in the future")
    @NotNull(message = "Joining date cannot be null")
    private LocalDate joiningDate;

    @Positive(message = "Salary must be positive")
    @NotNull(message = "Salary cannot be null")
    private BigDecimal salaryOffered;

    @NotBlank(message = "Document URL cannot be blank")
    private String documentUrl;

    private String termsAndConditions;
}

