package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.CandidateResponse;
import com.roze.smarthr.dto.InterviewResponse;
import com.roze.smarthr.dto.OfferLetterResponse;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.service.CandidateService;
import com.roze.smarthr.service.InterviewService;
import com.roze.smarthr.service.OfferLetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("candidate_portals")
@RequiredArgsConstructor
public class CandidatePortalController {
    private final CandidateService candidateService;
    private final InterviewService interviewService;
    private final OfferLetterService offerLetterService;

    @PreAuthorize("hasAuthority('CANDIDATE')")
    @GetMapping("/my_profile")
    public ResponseEntity<BaseResponse<CandidateResponse>> getMyProfile(
            @AuthenticationPrincipal User currentUser) {
        CandidateResponse response = candidateService.getCandidateByUser(currentUser);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAuthority('CANDIDATE')")
    @GetMapping("/my_interviews")
    public ResponseEntity<BaseResponse<List<InterviewResponse>>> getMyInterviews(
            @AuthenticationPrincipal User currentUser) {
        CandidateResponse candidate = candidateService.getCandidateByUser(currentUser);
        List<InterviewResponse> responses = interviewService.getInterviewsByCandidate(candidate.getId());
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAuthority('CANDIDATE')")
    @GetMapping("/my_offer")
    public ResponseEntity<BaseResponse<OfferLetterResponse>> getMyOffer(
            @AuthenticationPrincipal User currentUser) {
        CandidateResponse candidate = candidateService.getCandidateByUser(currentUser);
        OfferLetterResponse response = offerLetterService.getOfferLetterByCandidate(candidate.getId());
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAuthority('CANDIDATE')")
    @PostMapping("/accept_offer")
    public ResponseEntity<BaseResponse<OfferLetterResponse>> acceptOffer(
            @AuthenticationPrincipal User currentUser) {
        CandidateResponse candidate = candidateService.getCandidateByUser(currentUser);
        OfferLetterResponse response = offerLetterService.updateOfferLetterStatus(
                candidate.getOfferLetterId(), "ACCEPTED");
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Offer accepted successfully",
                response
        ));
    }

    @PreAuthorize("hasAuthority('CANDIDATE')")
    @PostMapping("/reject_offer")
    public ResponseEntity<BaseResponse<OfferLetterResponse>> rejectOffer(
            @AuthenticationPrincipal User currentUser) {
        CandidateResponse candidate = candidateService.getCandidateByUser(currentUser);
        OfferLetterResponse response = offerLetterService.updateOfferLetterStatus(
                candidate.getOfferLetterId(), "REJECTED");
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Offer rejected successfully",
                response
        ));
    }
}