package com.kahack.employeemanagementservice.util;

import com.kahack.employeemanagementservice.api.EmployeeResponse;
import com.kahack.employeemanagementservice.core.Department;
import com.kahack.employeemanagementservice.core.Employee;
import com.kahack.employeemanagementservice.core.Title;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.kahack.employeemanagementservice.util.DateUtil.formatter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ApiUtilTest {
    @Test
    public void testEmployeesConvertToEmployeeResponseWhenPresent() throws ParseException {
        List<Employee> employees = Arrays.asList(Employee.builder()
                .firstName("Dan")
                .lastName("Jackson")
                .startDate(formatter.parse("2015-01-22"))
                .department(Department.builder()
                        .name("Sales")
                        .build())
                .title(Title.builder()
                        .name("Manager")
                        .build())
                .build());

        List<EmployeeResponse> responses = ApiUtil.convertEmployeesToResponse(employees);

        assertThat(responses.size(), equalTo(1));
        assertThat(responses.get(0), equalTo(EmployeeResponse.builder()
                .firstName("Dan")
                .lastName("Jackson")
                .startDate("2015-01-22")
                .department("Sales")
                .title("Manager")
                .build()));
    }

    @Test
    public void testEmployeesConvertToEmployeeResponseWhenNonePresent(){
        List<Employee> employees = Collections.emptyList();

        List<EmployeeResponse> responses = ApiUtil.convertEmployeesToResponse(employees);

        assertThat(responses.size(), equalTo(0));
    }
}
