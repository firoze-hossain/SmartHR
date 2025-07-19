package com.roze.smarthr.dto;

import com.roze.smarthr.enums.CandidateStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CandidateResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String resumeUrl;
    private LocalDate appliedDate;
    private Long jobPostId;
    private String jobPostTitle;
    private CandidateStatus status;
    private Long userId;
    private Long offerLetterId;
}