package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.OfferLetterRequest;
import com.roze.smarthr.dto.OfferLetterResponse;
import com.roze.smarthr.entity.OfferLetter;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.enums.OfferLetterStatus;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.OfferLetterMapper;
import com.roze.smarthr.repository.OfferLetterRepository;
import com.roze.smarthr.service.NotificationService;
import com.roze.smarthr.service.OfferLetterService;
import com.roze.smarthr.utils.FileStorageService;
import com.roze.smarthr.utils.PdfGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OfferLetterServiceImpl implements OfferLetterService {
    private final OfferLetterRepository offerLetterRepository;
    private final OfferLetterMapper offerLetterMapper;
    private final NotificationService notificationService;
    private final PdfGenerationService pdfGenerationService;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public OfferLetterResponse createOfferLetter(OfferLetterRequest request, User currentUser) {
        // Check if candidate already has an offer letter
        if (offerLetterRepository.existsByCandidateId(request.getCandidateId())) {
            throw new IllegalStateException("Candidate already has an offer letter");
        }

        OfferLetter offerLetter = offerLetterMapper.toEntity(request, currentUser);
        // 3. Generate PDF
        byte[] pdfBytes = pdfGenerationService.generateOfferLetterPdf(offerLetter);

        // 4. Store PDF and get URL
        String fileName = "offer_" + offerLetter.getCandidate().getId() + ".pdf";
        String documentUrl = fileStorageService.storePdf(pdfBytes, fileName);
        OfferLetter savedOfferLetter = offerLetterRepository.save(offerLetter);

        // Send notification to candidate
        notificationService.sendOfferLetterNotification(
                savedOfferLetter.getCandidate().getEmail(),
                documentUrl,
                pdfBytes);

        return offerLetterMapper.toDto(savedOfferLetter);
    }

    @Override
    public OfferLetterResponse getOfferLetterById(Long id) {
        OfferLetter offerLetter = offerLetterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer letter not found with id: " + id));
        return offerLetterMapper.toDto(offerLetter);
    }

    @Override
    public OfferLetterResponse getOfferLetterByCandidate(Long candidateId) {
        OfferLetter offerLetter = offerLetterRepository.findByCandidateId(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer letter not found for candidate id: " + candidateId));
        return offerLetterMapper.toDto(offerLetter);
    }

    @Override
    @Transactional
    public OfferLetterResponse updateOfferLetterStatus(Long id, String status) {
        OfferLetter offerLetter = offerLetterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer letter not found with id: " + id));

        try {
            OfferLetterStatus newStatus = OfferLetterStatus.valueOf(status.toUpperCase());
            offerLetter.setStatus(newStatus);
            OfferLetter updatedOfferLetter = offerLetterRepository.save(offerLetter);

            // Notify HR about offer status change
            notificationService.sendOfferStatusNotification(
                    offerLetter.getIssuedBy().getEmail(),
                    offerLetter.getCandidate().getFullName(),
                    newStatus.toString());

            return offerLetterMapper.toDto(updatedOfferLetter);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    @Override
    public void sendOfferLetterEmail(Long offerLetterId) {
        OfferLetter offerLetter = offerLetterRepository.findById(offerLetterId)
                .orElseThrow(() -> new ResourceNotFoundException("Offer letter not found with id: " + offerLetterId));
        byte[] pdfBytes = pdfGenerationService.generateOfferLetterPdf(offerLetter);

        // 4. Store PDF and get URL
        String fileName = "offer_" + offerLetter.getCandidate().getId() + ".pdf";
        String documentUrl = fileStorageService.storePdf(pdfBytes, fileName);
        OfferLetter savedOfferLetter = offerLetterRepository.save(offerLetter);

        // Send notification to candidate
        notificationService.sendOfferLetterNotification(
                savedOfferLetter.getCandidate().getEmail(),
                documentUrl,
                pdfBytes);
    }


}