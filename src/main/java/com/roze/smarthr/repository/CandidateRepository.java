package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Candidate;
import com.roze.smarthr.enums.CandidateStatus;
import com.roze.smarthr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {
    List<Candidate> findByJobPostId(Long jobPostId);

    List<Candidate> findByStatus(CandidateStatus status);

    Optional<Candidate> findByEmail(String email);

    Optional<Candidate> findByUser(User user);

    @Query("SELECT c FROM Candidate c WHERE c.jobPost.department.id = :departmentId")
    List<Candidate> findByDepartmentId(@Param("departmentId") Long departmentId);
}