
package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Holiday;
import com.roze.smarthr.enums.HolidayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    List<Holiday> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Holiday> findByType(HolidayType type);

    List<Holiday> findByDate(LocalDate date);

    List<Holiday> findByRecurring(boolean recurring);

    @Query("SELECT h FROM Holiday h WHERE YEAR(h.date) = :year")
    List<Holiday> findByYear(@Param("year") int year);

    @Query("SELECT h FROM Holiday h WHERE h.region = :region AND h.date BETWEEN :startDate AND :endDate")
    List<Holiday> findByRegionAndDateBetween(
            @Param("region") String region,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    boolean existsByDateAndName(LocalDate date, String name);

    boolean existsByDate(LocalDate date);
}