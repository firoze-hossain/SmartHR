package com.roze.smarthr.service;

import com.roze.smarthr.dto.OfferLetterRequest;
import com.roze.smarthr.dto.OfferLetterResponse;
import com.roze.smarthr.entity.User;

public interface OfferLetterService {
    OfferLetterResponse createOfferLetter(OfferLetterRequest request, User currentUser);

    OfferLetterResponse getOfferLetterById(Long id);

    OfferLetterResponse getOfferLetterByCandidate(Long candidateId);

    OfferLetterResponse updateOfferLetterStatus(Long id, String status);

    void sendOfferLetterEmail(Long offerLetterId);

}