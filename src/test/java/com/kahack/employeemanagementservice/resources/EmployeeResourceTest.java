package com.kahack.employeemanagementservice.resources;

import com.kahack.employeemanagementservice.api.AddEmployeeRequest;
import com.kahack.employeemanagementservice.api.EmployeeResponse;
import com.kahack.employeemanagementservice.api.GetEmployeesInDepartmentResponse;
import com.kahack.employeemanagementservice.core.Department;
import com.kahack.employeemanagementservice.core.Employee;
import com.kahack.employeemanagementservice.core.Title;
import com.kahack.employeemanagementservice.repository.DepartmentRepository;
import com.kahack.employeemanagementservice.repository.EmployeeRepository;
import com.kahack.employeemanagementservice.repository.TitleRepository;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static com.kahack.employeemanagementservice.util.DateUtil.formatter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class EmployeeResourceTest {
    private static final EmployeeRepository emp = mock(EmployeeRepository.class);
    private static final DepartmentRepository dep = mock(DepartmentRepository.class);
    private static final TitleRepository title = mock(TitleRepository.class);
    public static final ResourceExtension ext = ResourceExtension.builder()
            .addResource(new EmployeeResource(emp, title, dep))
            .build();

    @AfterEach
    void tearDown() {
        reset(emp);
        reset(dep);
        reset(title);
    }

    @Test
    public void testGetEmployeesSuccessful() throws ParseException {
        when(dep.findDepartmentResourceByName("Tech"))
                .thenReturn(Optional.of(Department
                        .builder()
                        .name("Tech")
                        .departmentId("department_id")
                        .build()));
        when(emp.getEmployeesByDepartment("department_id"))
                .thenReturn(Arrays.asList(Employee.builder()
                        .firstName("Dan")
                        .lastName("Jackson")
                        .startDate(formatter.parse("2015-01-22"))
                        .department(Department.builder()
                                .name("Tech")
                                .build())
                        .title(Title.builder()
                                .name("Manager")
                                .build())
                        .build()));

        final Response response = ext.target("/employees/getEmployeesInDepartment")
                .queryParam("department", "Tech")
                .request()
                .get();

        assertThat(response.getStatus(), equalTo(Status.OK.getStatusCode()));
        assertThat(response.readEntity(GetEmployeesInDepartmentResponse.class), equalTo(GetEmployeesInDepartmentResponse
                .builder()
                .employees(Arrays.asList(EmployeeResponse.builder()
                                .firstName("Dan")
                                .lastName("Jackson")
                                .startDate("2015-01-22")
                                .title("Manager")
                                .department("Tech")
                                .build()))
                .build()));
    }

    @Test
    public void testGetEmployeesMissingDepartment() {
        when(dep.findDepartmentResourceByName("Sales"))
                .thenReturn(Optional.of(Department
                        .builder()
                        .name("Sales")
                        .departmentId("department_id")
                        .build()));

        final Response response = ext.target("/employees/getEmployeesInDepartment")
                .queryParam("department", "Tech")
                .request()
                .get();

        assertThat(response.getStatus(), equalTo(Status.NOT_FOUND.getStatusCode()));
        assertThat(response.readEntity(String.class), equalTo("Department doesn't exist"));
    }

    @Test
    public void testGetEmployeesNoParamForDepartment() {

        final Response response = ext.target("/employees/getEmployeesInDepartment")
                .request()
                .get();

        assertThat(response.getStatus(), equalTo(Status.BAD_REQUEST.getStatusCode()));
        assertThat(response.readEntity(String.class), equalTo("Department parameter cannot be empty"));
    }

    @Test
    public void testAddEmployeeSucess() throws ParseException {
        Date startDate = formatter.parse("2021-09-05");
        when(emp.duplicateEmployee(Employee.builder()
                .firstName("Dan")
                .lastName("Walter")
                .startDate(startDate)
                .build()))
                .thenReturn(false);

        when(title.fetchTitleId("Director")).thenReturn("title_id");
        when(dep.fetchDepartmentId("Tech")).thenReturn("department_id");
        final Response response = ext.target("/employees/addEmployee")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(AddEmployeeRequest.builder()
                        .firstName("Dan")
                        .lastName("Walter")
                        .startDate("2021-09-05")
                        .department("Tech")
                        .title("Director")
                        .build(), MediaType.APPLICATION_JSON_TYPE));


        assertThat(response.getStatus(), equalTo(Status.OK.getStatusCode()));
        assertThat(response.readEntity(String.class), equalTo("Successfully added employee"));
    }

    @Test
    public void testAddEmployeeDuplicate() throws ParseException {
        when(emp.duplicateEmployee(any()))
                .thenReturn(true);

        final Response response = ext.target("/employees/addEmployee")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(AddEmployeeRequest.builder()
                        .firstName("Dan")
                        .lastName("Walter")
                        .startDate("2021-09-05")
                        .department("Tech")
                        .title("Director")
                        .build(), MediaType.APPLICATION_JSON_TYPE));



        assertThat(response.getStatus(), equalTo(Status.BAD_REQUEST.getStatusCode()));
        assertThat(response.readEntity(String.class), equalTo("This employee already exists"));
    }

    @Test
    public void testAddEmployeeException() {
        when(emp.duplicateEmployee(any()))
                .thenReturn(false);
        when(title.fetchTitleId(any())).thenThrow(RuntimeException.class);

        final Response response = ext.target("/employees/addEmployee")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(AddEmployeeRequest.builder()
                        .firstName("Dan")
                        .lastName("Walter")
                        .startDate("2021-09-05")
                        .department("Tech")
                        .title("Director")
                        .build(), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus(), equalTo(Status.INTERNAL_SERVER_ERROR.getStatusCode()));
    }
}
