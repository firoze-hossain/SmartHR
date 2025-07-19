package com.roze.smarthr.repository;

import com.roze.smarthr.entity.JobPost;
import com.roze.smarthr.enums.JobPostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Long>, JpaSpecificationExecutor<JobPost> {
    List<JobPost> findByStatus(JobPostStatus status);

    @Query("SELECT j FROM JobPost j WHERE j.closingDate >= :currentDate AND j.status = 'OPEN'")
    List<JobPost> findActiveJobPosts(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT j FROM JobPost j WHERE j.department.id = :departmentId AND j.status = 'OPEN'")
    List<JobPost> findByDepartmentId(@Param("departmentId") Long departmentId);
}