package com.roze.smarthr.config;

import com.roze.smarthr.entity.User;
import com.roze.smarthr.service.UserPreferenceService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LocaleInterceptor implements HandlerInterceptor {
    private final UserPreferenceService userPreferenceService;
    private final MessageSource messageSource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Get authentication from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // Try to extract User from different possible principal types
            Optional<User> userOptional = extractUserFromPrincipal(authentication.getPrincipal());

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String languageCode = userPreferenceService.getUserLanguageCode(user);
                Locale locale = new Locale(languageCode);
                LocaleContextHolder.setLocale(locale);
            }
        }
        return true;
    }

    private Optional<User> extractUserFromPrincipal(Object principal) {
        if (principal instanceof User) {
            // Case 1: Principal is directly your User entity
            return Optional.of((User) principal);
        } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            // Case 2: Principal is Spring's UserDetails - you may need to load your User entity
            String username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            // Here you would typically fetch your User entity from repository
            // return userRepository.findByEmail(username);
            return Optional.empty(); // Replace with actual implementation
        }
        return Optional.empty();
    }
}