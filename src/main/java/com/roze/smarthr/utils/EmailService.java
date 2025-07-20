package com.roze.smarthr.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface EmailService {
    // Interview related emails
    void sendInterviewScheduledEmail(String recipientEmail, LocalDateTime scheduledDate, String meetingLink,String interviewType, boolean isCandidate,String calendarEventLink);

    void sendInterviewReminderEmail(String recipientEmail, LocalDateTime scheduledDate, String meetingLink);

    void sendInterviewResultEmail(String candidateEmail, String result, String feedback);

    // Offer letter emails
    void sendOfferLetterEmail(String candidateEmail, byte[] pdfAttachment, String documentUrl);

    void sendOfferStatusUpdateEmail(String hrEmail, String candidateName, String newStatus);

    // Onboarding emails
    void sendOnboardingTaskAssignmentEmail(String assigneeEmail, String taskName, LocalDate deadline);

    void sendOnboardingTaskCompletionEmail(String hrEmail, String candidateEmail, String taskName);

    void sendOnboardingTaskCompletionConfirmation(String candidateEmail, String taskName);

    void sendOnboardingTaskReminderEmail(String assigneeEmail, String taskName, LocalDate deadline);

    // Account related emails
    void sendCandidateAccountEmail(String candidateEmail, String tempPassword);

    void sendPasswordResetEmail(String userEmail, String resetToken);

    void sendAccountActivationEmail(String userEmail, String activationToken);

    // System emails
    void sendSystemAlertEmail(String alertMessage);

    void sendDailyDigestEmail(String recipientEmail, String digestContent);
}