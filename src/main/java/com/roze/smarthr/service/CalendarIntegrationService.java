package com.roze.smarthr.service;

import com.roze.smarthr.entity.Interview;

public interface CalendarIntegrationService {
    String createCalendarEvent(Interview interview);

    void updateCalendarEvent(Interview interview);

    void deleteCalendarEvent(String eventId);

    String getCalendarEventLink(String eventId);
}
