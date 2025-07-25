package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.UserPreferenceRequest;
import com.roze.smarthr.dto.UserPreferenceResponse;
import com.roze.smarthr.entity.User;
import com.roze.smarthr.service.UserPreferenceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user_preferences")
@RequiredArgsConstructor
public class UserPreferenceController {
    private final UserPreferenceService userPreferenceService;

    @PostMapping
    public ResponseEntity<BaseResponse<UserPreferenceResponse>> createOrUpdateUserPreference(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UserPreferenceRequest request) {
        UserPreferenceResponse response = userPreferenceService.createOrUpdateUserPreference(
                currentUser.getId(), request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "User preferences updated successfully",
                response
        ));
    }

    @GetMapping("/my")
    public ResponseEntity<BaseResponse<UserPreferenceResponse>> getMyPreferences(
            @AuthenticationPrincipal User currentUser) {
        UserPreferenceResponse response = userPreferenceService.getUserPreference(currentUser.getId());
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BaseResponse<UserPreferenceResponse>> getUserPreferences(
            @PathVariable Long userId) {
        UserPreferenceResponse response = userPreferenceService.getUserPreference(userId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }
}