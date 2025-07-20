package com.roze.smarthr.utils;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final EmailConfigProperties emailConfig;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    @Override
    @Async
    public void sendInterviewScheduledEmail(String recipientEmail, LocalDateTime scheduledDate, String meetingLink, String interviewType, boolean isCandidate, String calendarEventLink) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("interviewDate", scheduledDate.format(DATE_FORMATTER));
        context.setVariable("interviewTime", scheduledDate.format(TIME_FORMATTER));
        context.setVariable("meetingLink", meetingLink);
        context.setVariable("interviewType", interviewType);
        // Only use the generated template link if we don't have a real calendar event
        if (calendarEventLink == null || calendarEventLink.isEmpty()) {
            calendarEventLink = "https://www.google.com/calendar/render?action=TEMPLATE" +
                    "&text=Interview with " + (isCandidate ? "Company" : "Candidate") +
                    "&dates=" + formatGoogleCalendarDate(scheduledDate) + "/" +
                    formatGoogleCalendarDate(scheduledDate.plusHours(1)) +
                    "&details=Interview type: " + interviewType + "%0AJoin link: " + meetingLink +
                    "&location=" + meetingLink +
                    "&sf=true&output=xml";
        }

        context.setVariable("calendarEventLink", calendarEventLink);
        String subject;
        String template;

        if (isCandidate) {
            subject = "Interview Scheduled: " + scheduledDate.format(DATE_FORMATTER);
            template = "email/interview-scheduled-candidate"; // Candidate template
        } else {
            subject = "Interview Scheduled with Candidate";
            template = "email/interview-scheduled-interviewer"; // Interviewer template
            // Add any additional variables needed for interviewer template
        }

        sendHtmlEmail(recipientEmail, subject, template, context);
    }

    private String formatGoogleCalendarDate(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
    }

    @Override
    @Async
    public void sendInterviewReminderEmail(String recipientEmail, LocalDateTime scheduledDate, String meetingLink) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("interviewDate", scheduledDate.format(DATE_FORMATTER));
        context.setVariable("interviewTime", scheduledDate.format(TIME_FORMATTER));
        context.setVariable("meetingLink", meetingLink);

        String subject = "Reminder: Upcoming Interview Today at " + scheduledDate.format(TIME_FORMATTER);
        String template = "email/interview-reminder";

        sendHtmlEmail(recipientEmail, subject, template, context);
    }

    @Override
    @Async
    public void sendInterviewResultEmail(String candidateEmail, String result, String feedback) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("result", result);
        context.setVariable("feedback", feedback != null ? feedback : "No additional feedback provided");

        String subject = "Your Interview Result: " + result;
        String template = "email/interview-result";

        sendHtmlEmail(candidateEmail, subject, template, context);
    }

    @Override
    @Async
    public void sendOfferLetterEmail(String candidateEmail, String documentUrl) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("documentUrl", documentUrl);

        String subject = "Your Offer Letter from SmartHR";
        String template = "email/offer-letter";

        sendHtmlEmail(candidateEmail, subject, template, context);
    }

    @Override
    @Async
    public void sendOfferStatusUpdateEmail(String hrEmail, String candidateName, String newStatus) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("candidateName", candidateName);
        context.setVariable("newStatus", newStatus);

        String subject = "Offer Status Update: " + candidateName;
        String template = "email/offer-status-update";

        sendHtmlEmail(hrEmail, subject, template, context);
    }

    @Override
    @Async
    public void sendOnboardingTaskAssignmentEmail(String assigneeEmail, String taskName, LocalDate deadline) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("taskName", taskName);
        context.setVariable("deadline", deadline.format(DATE_FORMATTER));

        String subject = "New Onboarding Task Assigned: " + taskName;
        String template = "email/onboarding-task-assignment";

        sendHtmlEmail(assigneeEmail, subject, template, context);
    }

    @Override
    @Async
    public void sendOnboardingTaskCompletionEmail(String hrEmail, String candidateEmail, String taskName) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("candidateEmail", candidateEmail);
        context.setVariable("taskName", taskName);

        String subject = "Onboarding Task Completed: " + taskName;
        String template = "email/onboarding-task-completion";

        sendHtmlEmail(hrEmail, subject, template, context);
    }

    @Override
    @Async
    public void sendOnboardingTaskCompletionConfirmation(String candidateEmail, String taskName) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("taskName", taskName);

        String subject = "Task Completed: " + taskName;
        String template = "email/onboarding-task-confirmation";

        sendHtmlEmail(candidateEmail, subject, template, context);
    }

    @Override
    @Async
    public void sendOnboardingTaskReminderEmail(String assigneeEmail, String taskName, LocalDate deadline) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("taskName", taskName);
        context.setVariable("deadline", deadline.format(DATE_FORMATTER));

        String subject = "URGENT: Onboarding Task Reminder - " + taskName;
        String template = "email/onboarding-task-reminder";

        sendHtmlEmail(assigneeEmail, subject, template, context);
    }

    @Override
    @Async
    public void sendCandidateAccountEmail(String candidateEmail, String tempPassword) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("tempPassword", tempPassword);

        String subject = "Your SmartHR Candidate Account Credentials";
        String template = "email/candidate-account";

        sendHtmlEmail(candidateEmail, subject, template, context);
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String userEmail, String resetToken) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("resetToken", resetToken);
        context.setVariable("resetUrl", emailConfig.getPasswordResetUrl() + "?token=" + resetToken);

        String subject = "Password Reset Request for SmartHR Account";
        String template = "email/password-reset";

        sendHtmlEmail(userEmail, subject, template, context);
    }

    @Override
    @Async
    public void sendAccountActivationEmail(String userEmail, String activationToken) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("activationUrl", emailConfig.getAccountActivationUrl() + "?token=" + activationToken);

        String subject = "Activate Your SmartHR Account";
        String template = "email/account-activation";

        sendHtmlEmail(userEmail, subject, template, context);
    }

    @Override
    @Async
    public void sendSystemAlertEmail(String alertMessage) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("alertMessage", alertMessage);

        String subject = "SmartHR System Alert";
        String template = "email/system-alert";

        sendHtmlEmail(emailConfig.getAdminEmail(), subject, template, context);
    }

    @Override
    @Async
    public void sendDailyDigestEmail(String recipientEmail, String digestContent) {
        Context context = new Context(Locale.getDefault());
        context.setVariable("digestContent", digestContent);

        String subject = "Your SmartHR Daily Digest";
        String template = "email/daily-digest";

        sendHtmlEmail(recipientEmail, subject, template, context);
    }

    private void sendHtmlEmail(String to, String subject, String templateName, Context context) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            String htmlContent = templateEngine.process(templateName, context);

            helper.setFrom(emailConfig.getFrom());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email sent successfully to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}