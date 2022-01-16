package com.kahack.employeemanagementservice.core;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private String firstName;
    private String lastName;
    private Title title;
    private Date startDate;
    private Department department;
}
