package com.roze.smarthr.repository;

import com.roze.smarthr.entity.ShiftTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftTemplateRepository extends JpaRepository<ShiftTemplate, Long> {
    List<ShiftTemplate> findByActiveTrue();

    boolean existsByName(String name);
}