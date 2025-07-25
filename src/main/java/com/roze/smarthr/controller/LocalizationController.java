package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.BaseResponse;
import com.roze.smarthr.dto.LocalizedTextRequest;
import com.roze.smarthr.dto.LocalizedTextResponse;
import com.roze.smarthr.service.LocalizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("localizations")
@RequiredArgsConstructor
public class LocalizationController {
    private final LocalizationService localizationService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping
    public ResponseEntity<BaseResponse<LocalizedTextResponse>> createTranslation(
            @Valid @RequestBody LocalizedTextRequest request) {
        LocalizedTextResponse response = localizationService.createTranslation(request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<LocalizedTextResponse>> getTranslationById(@PathVariable Long id) {
        LocalizedTextResponse response = localizationService.getTranslationById(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<BaseResponse<List<LocalizedTextResponse>>> getTranslationsForEntity(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        List<LocalizedTextResponse> responses = localizationService.getTranslationsForEntity(entityType, entityId);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @GetMapping("/entity/{entityType}/{entityId}/{languageCode}")
    public ResponseEntity<BaseResponse<Map<String, String>>> getTranslationsForEntityAndLanguage(
            @PathVariable String entityType,
            @PathVariable Long entityId,
            @PathVariable String languageCode) {
        Map<String, String> response = localizationService.getTranslationsForEntityAndLanguage(
                entityType, entityId, languageCode);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<LocalizedTextResponse>> updateTranslation(
            @PathVariable Long id,
            @Valid @RequestBody LocalizedTextRequest request) {
        LocalizedTextResponse response = localizationService.updateTranslation(id, request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.UPDATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteTranslation(@PathVariable Long id) {
        localizationService.deleteTranslation(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.DELETE_SUCCESS,
                null
        ));
    }
}