package com.roze.smarthr.service.implementation;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.roze.smarthr.entity.Interview;
import com.roze.smarthr.exception.CalendarIntegrationException;
import com.roze.smarthr.service.CalendarIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CalendarIntegrationServiceImpl implements CalendarIntegrationService {
//    @Value("${google.calendar.application.name}")
//    private String applicationName;
//    @Value("${google.calendar.api.key}")
//    private String apiKey;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    @Value("${google.calendar.application.name}")
    private String applicationName;
    @Value("${google.calendar.client.id}")
    private String clientId;
    @Value("${google.calendar.client.secret}")
    private String clientSecret;
    @Value("${google.calendar.redirect.uri}")
    private String redirectUri;
    @Value("${google.calendar.scopes}")
    private String scope;

    //    @Override
//    public String createCalendarEvent(Interview interview) {
//        try {
//            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//            Calendar service = new Calendar.Builder(httpTransport, GsonFactory.getDefaultInstance(), null)
//                    .setApplicationName(applicationName)
//                    .build();
//
//            Event event = new Event()
//                    .setSummary("Interview with " + interview.getCandidate().getFullName())
//                    .setDescription(interview.getInterviewType() + " interview for position: " +
//                            interview.getCandidate().getJobPost().getTitle())
//                    .setLocation(interview.getMeetingLink());
//
//            LocalDateTime startDateTime = interview.getScheduledDate();
//            Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
//            EventDateTime start = new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(startDate));
//            event.setStart(start);
//
//            LocalDateTime endDateTime = startDateTime.plusHours(1);
//            Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
//            EventDateTime end = new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(endDate));
//            event.setEnd(end);
//
//            Event createdEvent = service.events().insert("primary", event)
//                    .setKey(apiKey)
//                    .execute();
//
//            return createdEvent.getId();
//        } catch (GeneralSecurityException | IOException e) {
//            throw new CalendarIntegrationException("Failed to create calendar event", e);
//        }
//    }
//
//    @Override
//    public void updateCalendarEvent(Interview interview) {
//        try {
//            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//            Calendar service = new Calendar.Builder(httpTransport, GsonFactory.getDefaultInstance(), null)
//                    .setApplicationName(applicationName)
//                    .build();
//
//            Event event = service.events().get("primary", interview.getCalendarEventId())
//                    .setKey(apiKey)
//                    .execute();
//
//            LocalDateTime startDateTime = interview.getScheduledDate();
//            Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
//            EventDateTime start = new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(startDate));
//            event.setStart(start);
//
//            LocalDateTime endDateTime = startDateTime.plusHours(1);
//            Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
//            EventDateTime end = new EventDateTime().setDateTime(new com.google.api.client.util.DateTime(endDate));
//            event.setEnd(end);
//
//            service.events().update("primary", interview.getCalendarEventId(), event)
//                    .setKey(apiKey)
//                    .execute();
//        } catch (GeneralSecurityException | IOException e) {
//            throw new CalendarIntegrationException("Failed to update calendar event", e);
//        }
//    }
//
//    @Override
//    public void deleteCalendarEvent(String eventId) {
//        try {
//            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//            Calendar service = new Calendar.Builder(httpTransport, GsonFactory.getDefaultInstance(), null)
//                    .setApplicationName(applicationName)
//                    .build();
//
//            service.events().delete("primary", eventId)
//                    .setKey(apiKey)
//                    .execute();
//        } catch (GeneralSecurityException | IOException e) {
//            throw new CalendarIntegrationException("Failed to delete calendar event", e);
//        }
//    }
    @Override
    public String createCalendarEvent(Interview interview) {
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                    .setApplicationName(applicationName)
                    .build();

            Event event = new Event()
                    .setSummary("Interview with " + interview.getCandidate().getFullName())
                    .setDescription(interview.getInterviewType() + " interview for position: " +
                            interview.getCandidate().getJobPost().getTitle())
                    .setLocation(interview.getMeetingLink());

            LocalDateTime startDateTime = interview.getScheduledDate();
            EventDateTime start = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(
                            Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant())));
            event.setStart(start);

            LocalDateTime endDateTime = startDateTime.plusHours(1);
            EventDateTime end = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(
                            Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant())));
            event.setEnd(end);

            Event createdEvent = service.events().insert("primary", event).execute();
            return createdEvent.getId();
        } catch (Exception e) {
            throw new CalendarIntegrationException("Failed to create calendar event", e);
        }
    }

    @Override
    public void updateCalendarEvent(Interview interview) {
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(httpTransport, GsonFactory.getDefaultInstance(), getCredentials(httpTransport))
                    .setApplicationName(applicationName)
                    .build();

            // Get the existing event
            Event event = service.events().get("primary", interview.getCalendarEventId()).execute();

            // Update event details
            LocalDateTime startDateTime = interview.getScheduledDate();
            EventDateTime start = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(
                            Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant())));
            event.setStart(start);

            LocalDateTime endDateTime = startDateTime.plusHours(1);
            EventDateTime end = new EventDateTime()
                    .setDateTime(new com.google.api.client.util.DateTime(
                            Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant())));
            event.setEnd(end);

            // Update the event
            service.events().update("primary", interview.getCalendarEventId(), event).execute();
        } catch (GeneralSecurityException | IOException e) {
            throw new CalendarIntegrationException("Failed to update calendar event", e);
        }
    }

    @Override
    public void deleteCalendarEvent(String eventId) {
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(httpTransport, GsonFactory.getDefaultInstance(), getCredentials(httpTransport))
                    .setApplicationName(applicationName)
                    .build();

            // Delete the event
            service.events().delete("primary", eventId).execute();
        } catch (GeneralSecurityException | IOException e) {
            throw new CalendarIntegrationException("Failed to delete calendar event", e);
        }
    }

    private Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        GoogleClientSecrets clientSecrets = new GoogleClientSecrets()
                .setWeb(new GoogleClientSecrets.Details()
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setRedirectUris(Collections.singletonList(redirectUri)));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, Collections.singleton(scope))
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8085).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    @Override
    public String getCalendarEventLink(String eventId) {
        if (eventId == null || eventId.isEmpty()) {
            return null;
        }
        return "https://www.google.com/calendar/event?eid=" + eventId;
    }
}