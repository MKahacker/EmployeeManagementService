package com.kahack.employeemanagementservice.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmployeeResponse {
    private final String firstName;
    private final String lastName;
    private final String startDate;
    private final String department;
    private final String title;
}
