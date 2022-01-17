package com.kahack.employeemanagementservice.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Objects;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private String firstName;
    private String lastName;
    private String startDate;
    private String department;
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeResponse that = (EmployeeResponse) o;
        return firstName.equals(that.firstName) && lastName.equals(that.lastName) && startDate.equals(that.startDate) && department.equals(that.department) && title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, startDate, department, title);
    }
}
