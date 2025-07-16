
package com.roze.smarthr.service;

import com.roze.smarthr.dto.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public interface SettingsService {
    // Global Settings
    List<GlobalSettingResponse> getAllGlobalSettings();

    GlobalSettingResponse getGlobalSettingByKey(String key);

    GlobalSettingResponse createGlobalSetting(GlobalSettingRequest request);

    GlobalSettingResponse updateGlobalSetting(String key, GlobalSettingRequest request);

    void deleteGlobalSetting(String key);

    // Working Days
    List<WorkingDayResponse> getAllWorkingDays();

    WorkingDayResponse getWorkingDay(DayOfWeek dayOfWeek);

    WorkingDayResponse updateWorkingDay(DayOfWeek dayOfWeek, WorkingDayRequest request);

    List<WorkingDayResponse> updateAllWorkingDays(List<WorkingDayRequest> requests);

    // Holidays
    List<HolidayResponse> getAllHolidays();

    List<HolidayResponse> getHolidaysByYear(int year);

    HolidayResponse getHolidayById(Long id);

    HolidayResponse createHoliday(HolidayRequest request);

    HolidayResponse updateHoliday(Long id, HolidayRequest request);

    void deleteHoliday(Long id);

    // Utility methods
    String getSettingValue(String key);

    boolean isWorkingDay(LocalDate date);

    boolean isHoliday(LocalDate date);
}