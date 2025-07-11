package com.roze.smarthr.repository;

import com.roze.smarthr.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    Boolean existsByTitleIgnoreCase(String title);
}
