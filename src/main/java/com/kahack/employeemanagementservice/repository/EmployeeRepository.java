package com.kahack.employeemanagementservice.repository;

import com.kahack.employeemanagementservice.core.Employee;

import java.util.List;

public interface EmployeeRepository {
    List<Employee> getEmployeesByDepartment(final String departmentId);
   void addEmployee(final Employee employee);
   boolean duplicateEmployee(final Employee employee);
}
