
package com.roze.smarthr.repository;

import com.roze.smarthr.entity.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {
    Optional<WorkingDay> findByDayOfWeek(DayOfWeek dayOfWeek);

    List<WorkingDay> findByIsWorking(boolean isWorking);
}