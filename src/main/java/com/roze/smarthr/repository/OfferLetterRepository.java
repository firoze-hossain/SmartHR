package com.roze.smarthr.repository;

import com.roze.smarthr.entity.OfferLetter;
import com.roze.smarthr.enums.OfferLetterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OfferLetterRepository extends JpaRepository<OfferLetter, Long>, JpaSpecificationExecutor<OfferLetter> {
    List<OfferLetter> findByStatus(OfferLetterStatus status);

    Optional<OfferLetter> findByCandidateId(Long candidateId);

    boolean existsByCandidateId(Long candidateId);
}