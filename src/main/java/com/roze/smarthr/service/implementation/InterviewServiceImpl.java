package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.InterviewRequest;
import com.roze.smarthr.dto.InterviewResponse;
import com.roze.smarthr.entity.Interview;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.enums.InterviewResult;
import com.roze.smarthr.exception.CalendarIntegrationException;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.mapper.InterviewMapper;
import com.roze.smarthr.repository.InterviewRepository;
import com.roze.smarthr.service.CalendarIntegrationService;
import com.roze.smarthr.service.InterviewService;
import com.roze.smarthr.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private final InterviewRepository interviewRepository;
    private final InterviewMapper interviewMapper;
    private final NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(InterviewServiceImpl.class);
    private final CalendarIntegrationService calendarIntegrationService;

    @Override
    @Transactional
    public InterviewResponse scheduleInterview(InterviewRequest request) {
        Interview interview = interviewMapper.toEntity(request);
        try {
            String eventId = calendarIntegrationService.createCalendarEvent(interview);
            interview.setCalendarEventId(eventId);
        } catch (CalendarIntegrationException e) {
            logger.error("Failed to create calendar event", e);
            // You can choose to proceed without calendar integration or throw exception
        }
        Interview savedInterview = interviewRepository.save(interview);

        // Send notifications
        notificationService.sendInterviewScheduledNotification(
                savedInterview.getCandidate().getEmail(),
                savedInterview.getInterviewer().getUser().getEmail(),
                savedInterview.getScheduledDate(),
                savedInterview.getMeetingLink(),
                savedInterview.getInterviewType().toString(),
                savedInterview.getCalendarEventId());

        return interviewMapper.toDto(savedInterview);
    }

    @Override
    public InterviewResponse getInterviewById(Long id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));
        return interviewMapper.toDto(interview);
    }

    @Override
    public List<InterviewResponse> getInterviewsByCandidate(Long candidateId) {
        return interviewRepository.findByCandidateId(candidateId).stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InterviewResponse> getUpcomingInterviews(User interviewer) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = now.plusDays(7); // Next 7 days
        return interviewRepository.findByInterviewerIdAndScheduledDateBetween(
                interviewer.getId(), now, endOfDay).stream()
                .map(interviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InterviewResponse submitInterviewFeedback(Long id, String feedback, String result) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with id: " + id));

        interview.setFeedback(feedback);
        try {
            interview.setResult(InterviewResult.valueOf(result.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid result: " + result);
        }

        Interview updatedInterview = interviewRepository.save(interview);

        // Send notification to HR about interview result
        notificationService.sendInterviewResultNotification(
                updatedInterview.getCandidate().getEmail(),
                updatedInterview.getResult().toString(),
                updatedInterview.getFeedback());

        return interviewMapper.toDto(updatedInterview);
    }

    @Override
    public void sendInterviewReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusHours(1); // Interviews happening in next hour

        List<Interview> upcomingInterviews = interviewRepository
                .findByScheduledDateBetween(now, reminderTime);

        for (Interview interview : upcomingInterviews) {
            notificationService.sendInterviewReminder(
                    interview.getCandidate().getEmail(),
                    interview.getInterviewer().getUser().getEmail(),
                    interview.getScheduledDate(),
                    interview.getMeetingLink());
        }
    }
}