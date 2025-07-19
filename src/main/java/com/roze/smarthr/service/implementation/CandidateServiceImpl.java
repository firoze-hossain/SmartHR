package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.CandidateRequest;
import com.roze.smarthr.dto.CandidateResponse;
import com.roze.smarthr.entity.Candidate;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.enums.CandidateStatus;
import com.roze.smarthr.exception.DuplicateResourceException;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.CandidateMapper;
import com.roze.smarthr.repository.CandidateRepository;
import com.roze.smarthr.service.AuthenticationService;
import com.roze.smarthr.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;
    private final AuthenticationService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CandidateResponse createCandidate(CandidateRequest request) {
        if (candidateRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Candidate already exists with email: " + request.getEmail());
        }

        Candidate candidate = candidateMapper.toEntity(request);
        Candidate savedCandidate = candidateRepository.save(candidate);
        return candidateMapper.toDto(savedCandidate);
    }

    @Override
    public CandidateResponse getCandidateById(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));
        return candidateMapper.toDto(candidate);
    }

    @Override
    public List<CandidateResponse> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(candidateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CandidateResponse> getCandidatesByJobPost(Long jobPostId) {
        return candidateRepository.findByJobPostId(jobPostId).stream()
                .map(candidateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CandidateResponse updateCandidateStatus(Long id, String status) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + id));

        try {
            CandidateStatus newStatus = CandidateStatus.valueOf(status.toUpperCase());
            candidate.setStatus(newStatus);
            Candidate updatedCandidate = candidateRepository.save(candidate);
            return candidateMapper.toDto(updatedCandidate);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    @Override
    @Transactional
    public CandidateResponse createCandidateUserAccount(Long candidateId, User currentUser) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));

        if (candidate.getUser() != null) {
            throw new DuplicateResourceException("Candidate already has a user account");
        }
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        User user = User.builder()
                .username(candidate.getEmail())
                .email(candidate.getEmail())
                .password(passwordEncoder.encode(tempPassword))
                .enabled(true)
                .build();

        User savedUser = userService.createUser(user, "CANDIDATE");
        candidate.setUser(savedUser);
        candidateRepository.save(candidate);

        // TODO: Send email with login credentials
        // emailService.sendCandidateAccountEmail(candidate.getEmail(), tempPassword);

        return candidateMapper.toDto(candidate);
    }

    @Override
    public CandidateResponse getCandidateByUser(User user) {
        Candidate candidate = candidateRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found for user id: " + user.getId()));
        return candidateMapper.toDto(candidate);
    }

    @Override
    public Long getOfferLetterId(Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id: " + candidateId));

        if (candidate.getOfferLetter() == null) {
            throw new ResourceNotFoundException("No offer letter found for candidate id: " + candidateId);
        }

        return candidate.getOfferLetter().getId();
    }
}