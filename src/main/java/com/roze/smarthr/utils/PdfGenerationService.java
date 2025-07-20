package com.roze.smarthr.utils;

import com.roze.smarthr.entity.OfferLetter;

public interface PdfGenerationService {
    byte[] generateOfferLetterPdf(OfferLetter offerLetter);
}
