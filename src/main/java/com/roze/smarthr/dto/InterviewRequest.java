package com.roze.smarthr.dto;

import com.roze.smarthr.enums.InterviewType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewRequest {
    @NotNull(message = "Candidate ID cannot be null")
    private Long candidateId;

    @NotNull(message = "Interviewer ID cannot be null")
    private Long interviewerId;

    @NotNull(message = "Interview type cannot be null")
    private InterviewType interviewType;

    @Future(message = "Interview date must be in the future")
    @NotNull(message = "Scheduled date cannot be null")
    private LocalDateTime scheduledDate;

    private String meetingLink;
}

