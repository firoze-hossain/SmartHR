package com.roze.smarthr.dto;

import com.roze.smarthr.enums.InterviewResult;
import com.roze.smarthr.enums.InterviewType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewResponse {
    private Long id;
    private Long candidateId;
    private String candidateName;
    private Long interviewerId;
    private String interviewerName;
    private InterviewType interviewType;
    private LocalDateTime scheduledDate;
    private String feedback;
    private InterviewResult result;
    private String meetingLink;
}