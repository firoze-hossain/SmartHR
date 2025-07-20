package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.OfferLetterRequest;
import com.roze.smarthr.dto.OfferLetterResponse;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.service.OfferLetterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("offer_letters")
@RequiredArgsConstructor
public class OfferLetterController {
    private final OfferLetterService offerLetterService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping
    public ResponseEntity<BaseResponse<OfferLetterResponse>> createOfferLetter(
            @Valid @RequestBody OfferLetterRequest request,
            @AuthenticationPrincipal User currentUser) {
        OfferLetterResponse response = offerLetterService.createOfferLetter(request, currentUser);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OfferLetterResponse>> getOfferLetterById(@PathVariable Long id) {
        OfferLetterResponse response = offerLetterService.getOfferLetterById(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<BaseResponse<OfferLetterResponse>> getOfferLetterByCandidate(
            @PathVariable Long candidateId) {
        OfferLetterResponse response = offerLetterService.getOfferLetterByCandidate(candidateId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BaseResponse<OfferLetterResponse>> updateOfferLetterStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        OfferLetterResponse response = offerLetterService.updateOfferLetterStatus(id, status);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Offer letter status updated successfully",
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping("/{id}/send")
    public ResponseEntity<BaseResponse<Void>> sendOfferLetterEmail(@PathVariable Long id) {
        offerLetterService.sendOfferLetterEmail(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Offer letter email sent successfully",
                null
        ));
    }
}