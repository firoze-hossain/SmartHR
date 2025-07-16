// SettingsServiceImpl.java
package com.roze.smarthr.service.implementation;

import com.roze.smarthr.dto.*;
import com.roze.smarthr.entity.GlobalSetting;
import com.roze.smarthr.entity.Holiday;
import com.roze.smarthr.entity.WorkingDay;
import com.roze.smarthr.exception.ResourceNotFoundException;
import com.roze.smarthr.exception.SettingsException;
import com.roze.smarthr.mapper.GlobalSettingMapper;
import com.roze.smarthr.mapper.HolidayMapper;
import com.roze.smarthr.mapper.WorkingDayMapper;
import com.roze.smarthr.repository.GlobalSettingRepository;
import com.roze.smarthr.repository.HolidayRepository;
import com.roze.smarthr.repository.WorkingDayRepository;
import com.roze.smarthr.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements SettingsService {
    private final GlobalSettingRepository globalSettingRepository;
    private final WorkingDayRepository workingDayRepository;
    private final HolidayRepository holidayRepository;
    private final GlobalSettingMapper globalSettingMapper;
    private final WorkingDayMapper workingDayMapper;
    private final HolidayMapper holidayMapper;

    // Global Settings Implementation
    @Override
    public List<GlobalSettingResponse> getAllGlobalSettings() {
        return globalSettingRepository.findAll().stream()
                .map(globalSettingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GlobalSettingResponse getGlobalSettingByKey(String key) {
        GlobalSetting setting = globalSettingRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Setting not found with key: " + key));
        return globalSettingMapper.toDto(setting);
    }

    @Override
    @Transactional
    public GlobalSettingResponse createGlobalSetting(GlobalSettingRequest request) {
        if (globalSettingRepository.existsByKey(request.getKey())) {
            throw new SettingsException("Setting with key '" + request.getKey() + "' already exists");
        }

        GlobalSetting setting = globalSettingMapper.toEntity(request);

        GlobalSetting savedSetting = globalSettingRepository.save(setting);
        return globalSettingMapper.toDto(savedSetting);
    }

    @Override
    @Transactional
    public GlobalSettingResponse updateGlobalSetting(String key, GlobalSettingRequest request) {
        GlobalSetting setting = globalSettingRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Setting not found with key: " + key));

        if (!setting.isEditable()) {
            throw new SettingsException("Setting with key '" + key + "' is not editable");
        }

        setting.setValue(request.getValue());
        setting.setDescription(request.getDescription());
        setting.setType(request.getType());
        setting.setEditable(request.isEditable());

        GlobalSetting updatedSetting = globalSettingRepository.save(setting);
        return globalSettingMapper.toDto(updatedSetting);
    }

    @Override
    @Transactional
    public void deleteGlobalSetting(String key) {
        GlobalSetting setting = globalSettingRepository.findByKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Setting not found with key: " + key));

        if (!setting.isEditable()) {
            throw new SettingsException("Setting with key '" + key + "' cannot be deleted");
        }

        globalSettingRepository.delete(setting);
    }

    // Working Days Implementation
    @Override
    public List<WorkingDayResponse> getAllWorkingDays() {
        return workingDayRepository.findAll().stream()
                .map(workingDayMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public WorkingDayResponse getWorkingDay(DayOfWeek dayOfWeek) {
        WorkingDay workingDay = workingDayRepository.findByDayOfWeek(dayOfWeek)
                .orElseThrow(() -> new ResourceNotFoundException("Working day configuration not found for: " + dayOfWeek));
        return workingDayMapper.toDto(workingDay);
    }

    @Override
    @Transactional
    public WorkingDayResponse updateWorkingDay(DayOfWeek dayOfWeek, WorkingDayRequest request) {
        WorkingDay workingDay = workingDayRepository.findByDayOfWeek(dayOfWeek)
                .orElseGet(() -> WorkingDay.builder().dayOfWeek(dayOfWeek).build());

        workingDay.setWorking(request.isWorking());
        WorkingDay updatedWorkingDay = workingDayRepository.save(workingDay);
        return workingDayMapper.toDto(updatedWorkingDay);
    }

    @Override
    @Transactional
    public List<WorkingDayResponse> updateAllWorkingDays(List<WorkingDayRequest> requests) {
        List<WorkingDay> workingDays = requests.stream()
                .map(request -> {
                    WorkingDay workingDay = workingDayRepository.findByDayOfWeek(request.getDayOfWeek())
                            .orElseGet(() -> WorkingDay.builder().dayOfWeek(request.getDayOfWeek()).build());
                    workingDay.setWorking(request.isWorking());
                    return workingDay;
                })
                .collect(Collectors.toList());

        List<WorkingDay> savedWorkingDays = workingDayRepository.saveAll(workingDays);
        return savedWorkingDays.stream()
                .map(workingDayMapper::toDto)
                .collect(Collectors.toList());
    }

    // Holidays Implementation
    @Override
    public List<HolidayResponse> getAllHolidays() {
        return holidayRepository.findAll().stream()
                .map(holidayMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HolidayResponse> getHolidaysByYear(int year) {
        return holidayRepository.findByYear(year).stream()
                .map(holidayMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public HolidayResponse getHolidayById(Long id) {
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found with id: " + id));
        return holidayMapper.toDto(holiday);
    }

    @Override
    @Transactional
    public HolidayResponse createHoliday(HolidayRequest request) {
        // Check if holiday already exists for this date
        if (holidayRepository.existsByDateAndName(request.getDate(), request.getName())) {
            throw new SettingsException("Holiday with same name and date already exists");
        }

        Holiday holiday = holidayMapper.toEntity(request);

        Holiday savedHoliday = holidayRepository.save(holiday);
        return holidayMapper.toDto(savedHoliday);
    }

    @Override
    @Transactional
    public HolidayResponse updateHoliday(Long id, HolidayRequest request) {
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found with id: " + id));

        holiday.setName(request.getName());
        holiday.setDate(request.getDate());
        holiday.setType(request.getType());
        holiday.setRegion(request.getRegion());
        holiday.setRecurring(request.isRecurring());

        Holiday updatedHoliday = holidayRepository.save(holiday);
        return holidayMapper.toDto(updatedHoliday);
    }

    @Override
    @Transactional
    public void deleteHoliday(Long id) {
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found with id: " + id));
        holidayRepository.delete(holiday);
    }

    // Utility Methods
    @Override
    public String getSettingValue(String key) {
        return globalSettingRepository.findByKey(key)
                .map(GlobalSetting::getValue)
                .orElseThrow(() -> new ResourceNotFoundException("Setting not found with key: " + key));
    }

    @Override
    public boolean isWorkingDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        Optional<WorkingDay> workingDay = workingDayRepository.findByDayOfWeek(dayOfWeek);

        // If no specific configuration exists, assume it's a working day
        if (workingDay.isEmpty()) {
            return true;
        }

        // Check if it's a configured working day and not a holiday
        return workingDay.get().isWorking() && !isHoliday(date);
    }

    @Override
    public boolean isHoliday(LocalDate date) {
        return holidayRepository.existsByDate(date);
    }
}