package com.roze.smarthr.service;

import com.roze.smarthr.dto.CreateNotificationDto;
import com.roze.smarthr.entity.*;
import com.roze.smarthr.enums.LeaveStatus;
import com.roze.smarthr.enums.NotificationPriority;
import com.roze.smarthr.enums.NotificationType;
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


}