package com.kahack.employeemanagementservice.repository;

import com.kahack.employeemanagementservice.core.Department;

import java.util.Optional;

public interface DepartmentRepository {
    String fetchDepartmentId(final String departmentName);
    Optional<Department> findDepartmentResourceByName(final String departmentName);
    void addDepartment(final Department department);
}
