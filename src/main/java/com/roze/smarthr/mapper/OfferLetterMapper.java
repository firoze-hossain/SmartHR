package com.roze.smarthr.mapper;

import com.roze.smarthr.dto.OfferLetterRequest;
import com.roze.smarthr.dto.OfferLetterResponse;
import com.roze.smarthr.entity.Candidate;
import com.roze.smarthr.entity.OfferLetter;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.enums.OfferLetterStatus;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OfferLetterMapper {
    private final CandidateRepository candidateRepository;

    public OfferLetter toEntity(OfferLetterRequest request, User issuedBy) {
        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + request.getCandidateId()));

        return OfferLetter.builder()
                .candidate(candidate)
                .offeredPosition(request.getOfferedPosition())
                .joiningDate(request.getJoiningDate())
                .salaryOffered(request.getSalaryOffered())
                .status(OfferLetterStatus.SENT)
                .issuedBy(issuedBy)
                .issueDate(LocalDate.now())
                .documentUrl(request.getDocumentUrl())
                .termsAndConditions(request.getTermsAndConditions())
                .build();
    }

    public OfferLetterResponse toDto(OfferLetter offerLetter) {
        return OfferLetterResponse.builder()
                .id(offerLetter.getId())
                .candidateId(offerLetter.getCandidate().getId())
                .candidateName(offerLetter.getCandidate().getFullName())
                .offeredPosition(offerLetter.getOfferedPosition())
                .joiningDate(offerLetter.getJoiningDate())
                .salaryOffered(offerLetter.getSalaryOffered())
                .status(offerLetter.getStatus())
                .issuedById(offerLetter.getIssuedBy().getId())
                .issuedByName(offerLetter.getIssuedBy().getUsername())
                .issueDate(offerLetter.getIssueDate())
                .documentUrl(offerLetter.getDocumentUrl())
                .build();
    }
}