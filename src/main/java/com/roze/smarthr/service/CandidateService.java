package com.roze.smarthr.service;

import com.roze.smarthr.dto.CandidateRequest;
import com.roze.smarthr.dto.CandidateResponse;
import com.roze.smarthr.entity.User;

import java.util.List;

public interface CandidateService {
    CandidateResponse createCandidate(CandidateRequest request);

    CandidateResponse getCandidateById(Long id);

    List<CandidateResponse> getAllCandidates();

    List<CandidateResponse> getCandidatesByJobPost(Long jobPostId);

    CandidateResponse updateCandidateStatus(Long id, String status);

    CandidateResponse createCandidateUserAccount(Long candidateId, User currentUser);


    CandidateResponse getCandidateByUser(User user);

    Long getOfferLetterId(Long candidateId);
}