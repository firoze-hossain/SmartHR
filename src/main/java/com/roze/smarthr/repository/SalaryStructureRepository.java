package com.roze.smarthr.repository;

import com.roze.smarthr.entity.SalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalaryStructureRepository extends JpaRepository<SalaryStructure, Long> {
    Optional<SalaryStructure> findByEmployeeId(Long employeeId);

    List<SalaryStructure> findByEffectiveToIsNull();
}
