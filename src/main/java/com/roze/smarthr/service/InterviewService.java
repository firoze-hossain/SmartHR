package com.roze.smarthr.service;

import com.roze.smarthr.dto.InterviewRequest;
import com.roze.smarthr.dto.InterviewResponse;
import com.roze.smarthr.entity.User;

import java.util.List;

public interface InterviewService {
    InterviewResponse scheduleInterview(InterviewRequest request);

    InterviewResponse getInterviewById(Long id);

    List<InterviewResponse> getInterviewsByCandidate(Long candidateId);

    List<InterviewResponse> getUpcomingInterviews(User interviewer);

    InterviewResponse submitInterviewFeedback(Long id, String feedback, String result);

    void sendInterviewReminders();
}