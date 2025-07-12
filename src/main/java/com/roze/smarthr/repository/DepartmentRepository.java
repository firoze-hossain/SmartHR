package com.roze.smarthr.repository;

import com.roze.smarthr.dto.DepartmentEmployeeCountDto;
import com.roze.smarthr.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    Boolean existsByTitleIgnoreCase(String title);

    @Query("SELECT NEW com.roze.smarthr.dto.DepartmentEmployeeCountDto(d.title, COUNT(e)) " +
            "FROM Department d LEFT JOIN d.employees e " +
            "GROUP BY d.title")
    List<DepartmentEmployeeCountDto> countEmployeesByDepartment();
}
