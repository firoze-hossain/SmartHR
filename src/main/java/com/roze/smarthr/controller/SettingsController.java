
package com.roze.smarthr.controller;

import com.roze.smarthr.constant.GlobalMessage;
import com.roze.smarthr.dto.*;
import com.roze.smarthr.service.SettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("settings")
@RequiredArgsConstructor
public class SettingsController {
    private final SettingsService settingsService;

    // Global Settings Endpoints
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping
    public ResponseEntity<BaseResponse<List<GlobalSettingResponse>>> getAllGlobalSettings() {
        List<GlobalSettingResponse> responses = settingsService.getAllGlobalSettings();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping("/{key}")
    public ResponseEntity<BaseResponse<GlobalSettingResponse>> getGlobalSettingByKey(@PathVariable String key) {
        GlobalSettingResponse response = settingsService.getGlobalSettingByKey(key);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<BaseResponse<GlobalSettingResponse>> createGlobalSetting(
            @Valid @RequestBody GlobalSettingRequest request) {
        GlobalSettingResponse response = settingsService.createGlobalSetting(request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PutMapping("/{key}")
    public ResponseEntity<BaseResponse<GlobalSettingResponse>> updateGlobalSetting(
            @PathVariable String key,
            @Valid @RequestBody GlobalSettingRequest request) {
        GlobalSettingResponse response = settingsService.updateGlobalSetting(key, request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.UPDATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{key}")
    public ResponseEntity<BaseResponse<Void>> deleteGlobalSetting(@PathVariable String key) {
        settingsService.deleteGlobalSetting(key);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.DELETE_SUCCESS,
                null
        ));
    }

    // Working Days Endpoints
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping("/working_days")
    public ResponseEntity<BaseResponse<List<WorkingDayResponse>>> getAllWorkingDays() {
        List<WorkingDayResponse> responses = settingsService.getAllWorkingDays();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping("/working_days/{dayOfWeek}")
    public ResponseEntity<BaseResponse<WorkingDayResponse>> getWorkingDay(
            @PathVariable DayOfWeek dayOfWeek) {
        WorkingDayResponse response = settingsService.getWorkingDay(dayOfWeek);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PutMapping("/working_days/{dayOfWeek}")
    public ResponseEntity<BaseResponse<WorkingDayResponse>> updateWorkingDay(
            @PathVariable DayOfWeek dayOfWeek,
            @Valid @RequestBody WorkingDayRequest request) {
        WorkingDayResponse response = settingsService.updateWorkingDay(dayOfWeek, request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.UPDATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PutMapping("/working_days")
    public ResponseEntity<BaseResponse<List<WorkingDayResponse>>> updateAllWorkingDays(
            @Valid @RequestBody List<WorkingDayRequest> requests) {
        List<WorkingDayResponse> responses = settingsService.updateAllWorkingDays(requests);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.UPDATE_SUCCESS,
                responses
        ));
    }

    // Holidays Endpoints
    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping("/holidays")
    public ResponseEntity<BaseResponse<List<HolidayResponse>>> getAllHolidays() {
        List<HolidayResponse> responses = settingsService.getAllHolidays();
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping("/holidays/year/{year}")
    public ResponseEntity<BaseResponse<List<HolidayResponse>>> getHolidaysByYear(
            @PathVariable int year) {
        List<HolidayResponse> responses = settingsService.getHolidaysByYear(year);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                responses
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @GetMapping("/holidays/{id}")
    public ResponseEntity<BaseResponse<HolidayResponse>> getHolidayById(
            @PathVariable Long id) {
        HolidayResponse response = settingsService.getHolidayById(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.READ_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PostMapping("/holidays")
    public ResponseEntity<BaseResponse<HolidayResponse>> createHoliday(
            @Valid @RequestBody HolidayRequest request) {
        HolidayResponse response = settingsService.createHoliday(request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.CREATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @PutMapping("/holidays/{id}")
    public ResponseEntity<BaseResponse<HolidayResponse>> updateHoliday(
            @PathVariable Long id,
            @Valid @RequestBody HolidayRequest request) {
        HolidayResponse response = settingsService.updateHoliday(id, request);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.UPDATE_SUCCESS,
                response
        ));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'HR')")
    @DeleteMapping("/holidays/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteHoliday(
            @PathVariable Long id) {
        settingsService.deleteHoliday(id);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                GlobalMessage.DELETE_SUCCESS,
                null
        ));
    }

    // Utility Endpoints
    @GetMapping("/is_working_day")
    public ResponseEntity<BaseResponse<Boolean>> isWorkingDay(
            @RequestParam LocalDate date) {
        boolean isWorkingDay = settingsService.isWorkingDay(date);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Working day status retrieved successfully",
                isWorkingDay
        ));
    }

    @GetMapping("/is_holiday")
    public ResponseEntity<BaseResponse<Boolean>> isHoliday(
            @RequestParam LocalDate date) {
        boolean isHoliday = settingsService.isHoliday(date);
        return ResponseEntity.ok(new BaseResponse<>(
                true,
                "Holiday status retrieved successfully",
                isHoliday
        ));
    }
}