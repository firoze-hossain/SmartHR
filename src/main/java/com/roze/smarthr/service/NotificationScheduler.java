package com.roze.smarthr.service;

import com.roze.smarthr.dto.CreateNotificationDto;
import com.roze.smarthr.entity.*;
import com.roze.smarthr.enums.LeaveStatus;
import com.roze.smarthr.enums.NotificationPriority;
import com.roze.smarthr.enums.NotificationType;
import com.roze.smarthr.enums.TrainingStatus;
import com.roze.smarthr.exception.NotificationException;
import com.roze.smarthr.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationScheduler {
    private final EmployeeRepository employeeRepository;
    private final RosterAssignmentRepository rosterAssignmentRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final EmployeeTrainingRepository employeeTrainingRepository;
    private final TrainingProgramRepository trainingProgramRepository;

    // Birthday notifications at 9 AM daily
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendBirthdayNotifications() {
        try {
            LocalDate today = LocalDate.now();
            List<Employee> birthdayEmployees = employeeRepository.findByBirthDate(today.getMonthValue(), today.getDayOfMonth());

            if (!birthdayEmployees.isEmpty()) {
                List<CreateNotificationDto> notifications = birthdayEmployees.stream()
                        .map(employee -> CreateNotificationDto.builder()
                                .recipientId(employee.getUser().getId())
                                .title("Happy Birthday! 🎉")
                                .message("Wishing you a wonderful birthday, " + employee.getName() + "!")
                                .type(NotificationType.BIRTHDAY)
                                .priority(NotificationPriority.LOW)
                                .build())
                        .collect(Collectors.toList());

                notificationService.sendBulkNotifications(notifications);
                log.info("Sent birthday notifications to {} employees", birthdayEmployees.size());
            }
        } catch (Exception e) {
            log.error("Failed to send birthday notifications: {}", e.getMessage());
            throw new NotificationException("Failed to send birthday notifications", e);
        }
    }

    // Shift reminders at 8 AM daily for next day's shifts
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendShiftReminders() {
        try {
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            List<Employee> employeesWithShift = employeeRepository.findEmployeesWithShiftOnDate(tomorrow);

            if (!employeesWithShift.isEmpty()) {
                List<CreateNotificationDto> notifications = employeesWithShift.stream()
                        .map(employee -> {
                            RosterAssignment assignment = rosterAssignmentRepository
                                    .findByEmployeeAndAssignmentDate(employee, tomorrow)
                                    .orElseThrow(() -> new NotificationException("Shift assignment not found"));

                            return CreateNotificationDto.builder()
                                    .recipientId(employee.getUser().getId())
                                    .title("Shift Reminder")
                                    .message("You have a " + assignment.getShiftTemplate().getName() + " scheduled for tomorrow")
                                    .type(NotificationType.SHIFT)
                                    .priority(NotificationPriority.MEDIUM)
                                    .actionUrl("/my-schedule")
                                    .build();
                        })
                        .collect(Collectors.toList());

                notificationService.sendBulkNotifications(notifications);
                log.info("Sent shift reminders to {} employees", employeesWithShift.size());
            }
        } catch (Exception e) {
            log.error("Failed to send shift reminders: {}", e.getMessage());
            throw new NotificationException("Failed to send shift reminders", e);
        }
    }

    // Leave approval reminders at 10 AM daily for pending leaves
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendLeaveApprovalReminders() {
        try {
            List<LeaveRequest> pendingLeaves = leaveRequestRepository.findByStatusOrderByFromDateAsc(LeaveStatus.PENDING);

            if (pendingLeaves.isEmpty()) {
                log.info("No pending leaves found for approval reminders");
                return;
            }

            List<User> hrAndAdminUsers = userRepository.findAllHrAndAdminUsers();
            if (hrAndAdminUsers.isEmpty()) {
                throw new NotificationException("No HR/Admin users found to send leave approval reminders");
            }

            List<CreateNotificationDto> notifications = new ArrayList<>();

            for (LeaveRequest leave : pendingLeaves) {
                String message = String.format("%s has a pending %s leave request from %s to %s",
                        leave.getEmployee().getName(),
                        leave.getLeaveType().getName(),
                        leave.getFromDate(),
                        leave.getToDate());

                Employee manager = leave.getEmployee().getDepartment().getManager();
                List<User> recipients = new ArrayList<>();

                if (manager != null && manager.getUser() != null) {
                    recipients.add(manager.getUser());
                    log.debug("Found manager for department {}: {}",
                            leave.getEmployee().getDepartment().getTitle(),
                            manager.getName());
                } else {
                    recipients.addAll(hrAndAdminUsers);
                    log.debug("No manager found for department {}, using HR/Admin fallback",
                            leave.getEmployee().getDepartment().getTitle());
                }

                for (User recipient : recipients) {
                    notifications.add(CreateNotificationDto.builder()
                            .recipientId(recipient.getId())
                            .title("Leave Approval Pending")
                            .message(message)
                            .type(NotificationType.LEAVE)
                            .priority(NotificationPriority.HIGH)
                            .actionUrl("/leave-approvals/" + leave.getId())
                            .build());
                }
            }

            if (!notifications.isEmpty()) {
                notificationService.sendBulkNotifications(notifications);
                log.info("Sent {} leave approval reminders", notifications.size());
            }
        } catch (Exception e) {
            log.error("Failed to send leave approval reminders: {}", e.getMessage());
            throw new NotificationException("Failed to send leave approval reminders", e);
        }
    }

    // Upcoming leave reminders at 5 PM daily for leaves starting in 2 days
    @Scheduled(cron = "0 0 17 * * ?")
    public void sendUpcomingLeaveReminders() {
        try {
            LocalDate inTwoDays = LocalDate.now().plusDays(2);
            List<LeaveRequest> approvedLeaves = leaveRequestRepository
                    .findByStatusAndFromDate(LeaveStatus.APPROVED, inTwoDays);

            if (!approvedLeaves.isEmpty()) {
                List<CreateNotificationDto> notifications = approvedLeaves.stream()
                        .map(leave -> CreateNotificationDto.builder()
                                .recipientId(leave.getEmployee().getUser().getId())
                                .title("Upcoming Leave Reminder")
                                .message("Your approved leave starts in 2 days (" +
                                        leave.getFromDate() + " to " + leave.getToDate() + ")")
                                .type(NotificationType.LEAVE)
                                .priority(NotificationPriority.MEDIUM)
                                .actionUrl("/my-leaves")
                                .build())
                        .collect(Collectors.toList());

                notificationService.sendBulkNotifications(notifications);
                log.info("Sent upcoming leave reminders to {} employees", approvedLeaves.size());
            }
        } catch (Exception e) {
            log.error("Failed to send upcoming leave reminders: {}", e.getMessage());
            throw new NotificationException("Failed to send upcoming leave reminders", e);
        }
    }

    // Monthly leave balance summary on 1st of each month at 9 AM
    @Scheduled(cron = "0 0 9 1 * ?")
    public void sendMonthlyLeaveBalanceSummary() {
        try {
            LocalDate today = LocalDate.now();
            List<Employee> allEmployees = employeeRepository.findAll();

            List<CreateNotificationDto> notifications = allEmployees.stream()
                    .map(employee -> {
                        long leavesTakenThisYear = leaveRequestRepository.countByEmployeeAndStatusAndFromDateBetween(
                                employee,
                                LeaveStatus.APPROVED,
                                LocalDate.of(today.getYear(), 1, 1),
                                today);

                        return CreateNotificationDto.builder()
                                .recipientId(employee.getUser().getId())
                                .title("Monthly Leave Summary")
                                .message("You've taken " + leavesTakenThisYear + " leave days this year")
                                .type(NotificationType.LEAVE)
                                .priority(NotificationPriority.LOW)
                                .actionUrl("/leave-balance")
                                .build();
                    })
                    .collect(Collectors.toList());

            notificationService.sendBulkNotifications(notifications);
            log.info("Sent monthly leave balance summaries to {} employees", allEmployees.size());
        } catch (Exception e) {
            log.error("Failed to send monthly leave balance summaries: {}", e.getMessage());
            throw new NotificationException("Failed to send monthly leave balance summaries", e);
        }
    }


    @Scheduled(cron = "0 0 9 * * ?")
    public void sendAttendanceNotifications() {
        try {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            List<Attendance> lateArrivals = attendanceRepository.findByDateAndLate(yesterday, true);
            List<Attendance> earlyDepartures = attendanceRepository.findByDateAndEarlyExit(yesterday, true);

            // Notify employees
            lateArrivals.forEach(attendance -> {
                try {
                    notificationService.createNotification(CreateNotificationDto.builder()
                            .recipientId(attendance.getEmployee().getUser().getId())
                            .title("Late Arrival Recorded")
                            .message("You were late on " + attendance.getDate() +
                                    ". Check-in time: " + attendance.getCheckIn().toLocalTime())
                            .type(NotificationType.ATTENDANCE)
                            .priority(NotificationPriority.MEDIUM)
                            .actionUrl("/my-attendance")
                            .build());
                } catch (Exception e) {
                    log.error("Failed to send late arrival notification to employee {}: {}",
                            attendance.getEmployee().getId(), e.getMessage());
                }
            });

            earlyDepartures.forEach(attendance -> {
                try {
                    notificationService.createNotification(CreateNotificationDto.builder()
                            .recipientId(attendance.getEmployee().getUser().getId())
                            .title("Early Departure Recorded")
                            .message("You left early on " + attendance.getDate() +
                                    ". Check-out time: " + attendance.getCheckOut().toLocalTime())
                            .type(NotificationType.ATTENDANCE)
                            .priority(NotificationPriority.MEDIUM)
                            .actionUrl("/my-attendance")
                            .build());
                } catch (Exception e) {
                    log.error("Failed to send early departure notification to employee {}: {}",
                            attendance.getEmployee().getId(), e.getMessage());
                }
            });

            // Notify HR/Admin
            if (!lateArrivals.isEmpty() || !earlyDepartures.isEmpty()) {
                List<User> hrAndAdminUsers = userRepository.findAllHrAndAdminUsers();
                if (hrAndAdminUsers.isEmpty()) {
                    throw new NotificationException("No HR/Admin users found to send attendance reports");
                }

                String message = String.format(
                        "Attendance issues yesterday:\nLate arrivals: %d\nEarly departures: %d",
                        lateArrivals.size(),
                        earlyDepartures.size()
                );

                hrAndAdminUsers.forEach(user -> {
                    try {
                        notificationService.createNotification(CreateNotificationDto.builder()
                                .recipientId(user.getId())
                                .title("Daily Attendance Report")
                                .message(message)
                                .type(NotificationType.ATTENDANCE)
                                .priority(NotificationPriority.MEDIUM)
                                .actionUrl("/attendance-reports")
                                .build());
                    } catch (Exception e) {
                        log.error("Failed to send attendance report to HR/Admin user {}: {}",
                                user.getId(), e.getMessage());
                    }
                });

                log.info("Sent attendance reports to {} HR/Admin users", hrAndAdminUsers.size());
            }
        } catch (Exception e) {
            log.error("Failed to send attendance notifications: {}", e.getMessage());
            throw new NotificationException("Failed to send attendance notifications", e);
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void sendUpcomingTrainingReminders() {
        try {
            LocalDate threeDaysLater = LocalDate.now().plusDays(3);
            List<EmployeeTraining> upcomingTrainings = employeeTrainingRepository
                    .findByStatusAndTrainingProgram_StartDate(TrainingStatus.ENROLLED, threeDaysLater);

            if (!upcomingTrainings.isEmpty()) {
                List<CreateNotificationDto> notifications = upcomingTrainings.stream()
                        .map(et -> CreateNotificationDto.builder()
                                .recipientId(et.getEmployee().getUser().getId())
                                .title("Upcoming Training Reminder")
                                .message(String.format("Your training '%s' is scheduled in 3 days on %s at %s",
                                        et.getTrainingProgram().getTitle(),
                                        et.getTrainingProgram().getStartDate(),
                                        et.getTrainingProgram().getLocation()))
                                .type(NotificationType.TRAINING_REMINDER)
                                .priority(NotificationPriority.MEDIUM)
                                .actionUrl("/my-trainings")
                                .build())
                        .collect(Collectors.toList());

                notificationService.sendBulkNotifications(notifications);
                log.info("Sent {} upcoming training reminders", notifications.size());
            }
        } catch (Exception e) {
            log.error("Failed to send training reminders: {}", e.getMessage());
            throw new NotificationException("Failed to send training reminders", e);
        }
    }

    // Daily at 10 AM - Feedback requests for completed trainings
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendTrainingFeedbackRequests() {
        try {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            List<EmployeeTraining> completedTrainings = employeeTrainingRepository
                    .findByStatusAndCompletionDateAndFeedbackSubmittedFalse(
                            TrainingStatus.COMPLETED, yesterday);

            if (!completedTrainings.isEmpty()) {
                List<CreateNotificationDto> notifications = completedTrainings.stream()
                        .map(et -> CreateNotificationDto.builder()
                                .recipientId(et.getEmployee().getUser().getId())
                                .title("Training Feedback Request")
                                .message(String.format("Please provide feedback for your completed training '%s'",
                                        et.getTrainingProgram().getTitle()))
                                .type(NotificationType.TRAINING_FEEDBACK_REQUEST)
                                .priority(NotificationPriority.MEDIUM)
                                .actionUrl("/training-feedback/" + et.getId())
                                .build())
                        .collect(Collectors.toList());

                notificationService.sendBulkNotifications(notifications);
                log.info("Sent {} training feedback requests", notifications.size());
            }
        } catch (Exception e) {
            log.error("Failed to send training feedback requests: {}", e.getMessage());
            throw new NotificationException("Failed to send training feedback requests", e);
        }
    }

    // Daily at 11 AM - Notify HR about mandatory training compliance
    @Scheduled(cron = "0 0 11 * * ?")
    public void sendMandatoryTrainingComplianceReport() {
        try {
            List<EmployeeTraining> incompleteMandatoryTrainings = employeeTrainingRepository
                    .findByTrainingProgram_MandatoryAndStatusNot(true, TrainingStatus.COMPLETED);

            if (!incompleteMandatoryTrainings.isEmpty()) {
                List<User> hrUsers = userRepository.findAllHrAndAdminUsers();
                if (hrUsers.isEmpty()) {
                    throw new NotificationException("No HR users found to send training compliance report");
                }

                String message = String.format(
                        "There are %d incomplete mandatory trainings:\n%s",
                        incompleteMandatoryTrainings.size(),
                        incompleteMandatoryTrainings.stream()
                                .map(et -> String.format("- %s: %s (%s)",
                                        et.getEmployee().getName(),
                                        et.getTrainingProgram().getTitle(),
                                        et.getStatus()))
                                .collect(Collectors.joining("\n"))
                );

                List<CreateNotificationDto> notifications = hrUsers.stream()
                        .map(user -> CreateNotificationDto.builder()
                                .recipientId(user.getId())
                                .title("Mandatory Training Compliance Report")
                                .message(message)
                                .type(NotificationType.TRAINING_REMINDER)
                                .priority(NotificationPriority.HIGH)
                                .actionUrl("/training-compliance")
                                .build())
                        .collect(Collectors.toList());

                notificationService.sendBulkNotifications(notifications);
                log.info("Sent training compliance report to {} HR users", hrUsers.size());
            }
        } catch (Exception e) {
            log.error("Failed to send training compliance report: {}", e.getMessage());
            throw new NotificationException("Failed to send training compliance report", e);
        }
    }
}