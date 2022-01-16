package com.kahack.employeemanagementservice.util;

import com.kahack.employeemanagementservice.api.EmployeeResponse;
import com.kahack.employeemanagementservice.core.Employee;

import java.util.List;
import java.util.stream.Collectors;

import static com.kahack.employeemanagementservice.util.DateUtil.formatter;

public class ApiUtil {
    public  static List<EmployeeResponse> convertEmployeesToResponse(List<Employee> employees) {
        return employees.stream()
                .map(employee -> EmployeeResponse.builder()
                        .firstName(employee.getFirstName())
                        .lastName(employee.getLastName())
                        .startDate(formatter.format(employee.getStartDate()))
                        .department(employee.getDepartment().getName())
                        .title(employee.getTitle().getName())
                        .build())
                .collect(Collectors.toList());

    }
}
