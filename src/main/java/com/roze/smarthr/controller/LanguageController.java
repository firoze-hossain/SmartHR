package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.LanguageRequest;
import com.roze.smarthr.dto.LanguageResponse;
import com.roze.smarthr.service.LanguageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponse<LanguageResponse>> createLanguage(
            @Valid @RequestBody LanguageRequest request) {
        LanguageResponse response = languageService.createLanguage(request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<LanguageResponse>> getLanguageById(@PathVariable Long id) {
        LanguageResponse response = languageService.getLanguageById(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<LanguageResponse>>> getAllLanguages() {
        List<LanguageResponse> responses = languageService.getAllLanguages();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/active")
    public ResponseEntity<BaseResponse<List<LanguageResponse>>> getActiveLanguages() {
        List<LanguageResponse> responses = languageService.getActiveLanguages();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<LanguageResponse>> updateLanguage(
            @PathVariable Long id,
            @Valid @RequestBody LanguageRequest request) {
        LanguageResponse response = languageService.updateLanguage(id, request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.UPDATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/set_default")
    public ResponseEntity<BaseResponse<Void>> setDefaultLanguage(@PathVariable Long id) {
        languageService.setDefaultLanguage(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Language set as default successfully",
                null
        ));
    }

    @GetMapping("/default")
    public ResponseEntity<BaseResponse<LanguageResponse>> getDefaultLanguage() {
        LanguageResponse response = languageService.getDefaultLanguage();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }
}