package com.kahack.employeemanagementservice.api;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GetEmployeesInDepartmentResponse {
    private List<EmployeeResponse> employees;
}
