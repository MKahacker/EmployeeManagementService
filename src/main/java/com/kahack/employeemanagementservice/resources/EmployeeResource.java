package com.kahack.employeemanagementservice.resources;

import com.codahale.metrics.annotation.Timed;
import com.kahack.employeemanagementservice.api.AddEmployeeRequest;
import com.kahack.employeemanagementservice.api.GetEmployeesInDepartmentResponse;
import com.kahack.employeemanagementservice.core.Employee;
import com.kahack.employeemanagementservice.core.Department;
import com.kahack.employeemanagementservice.core.Title;
import com.kahack.employeemanagementservice.exception.MissingDepartmentException;
import com.kahack.employeemanagementservice.repository.DepartmentRepository;
import com.kahack.employeemanagementservice.repository.EmployeeRepository;
import com.kahack.employeemanagementservice.repository.TitleRepository;
import com.kahack.employeemanagementservice.util.ApiUtil;
import lombok.Builder;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Optional;

import static com.kahack.employeemanagementservice.util.DateUtil.formatter;


@Path("/employees")
@Produces(MediaType.APPLICATION_JSON)
@Builder
public class EmployeeResource {
    private final Jdbi jdbi;
    private final EmployeeRepository employeeRepository;
    private final TitleRepository titleRepository;
    private final DepartmentRepository departmentRepository;

    @GET
    @Timed
    @Path("/getEmployeesInDepartment")
    public Response getEmployees(@QueryParam("department") Optional<String> departmentName) {
        if (!departmentName.isPresent()) {
            return Response
                    .status(Status.BAD_REQUEST)
                    .entity("Department parameter cannot be empty")
                    .build();
        }

        try {
            List<Employee> employees = jdbi.withHandle(handle -> {
                Optional<Department> department = departmentRepository.findDepartmentResourceByName(departmentName.get());

                if (!department.isPresent()) {
                    throw new MissingDepartmentException();
                }

                return employeeRepository.getEmployeesByDepartment(department.get().getDepartmentId());
            });

            return Response.ok(GetEmployeesInDepartmentResponse.builder()
                    .employees(ApiUtil.convertEmployeesToResponse(employees))
                    .build()).build();
        } catch (MissingDepartmentException ex) {
            return Response.status(Status.NOT_FOUND).entity("Department doesn't exist").build();
        }
    }

    @POST
    @Timed
    @Path("/addEmployee")
    public Response addEmployee(AddEmployeeRequest employeeRequest) {
        System.out.println(
                String.format("Got AddEmployeeRequest with Name: %s %s, Start Date: %s, %s, %s",
                        employeeRequest.getFirstName(),
                        employeeRequest.getLastName(),
                        employeeRequest.getStartDate(),
                        employeeRequest.getDepartment(),
                        employeeRequest.getTitle()));
        try {
            employeeRepository.addEmployee(Employee.builder()
                    .firstName(employeeRequest.getFirstName())
                    .lastName(employeeRequest.getLastName())
                    .startDate(formatter.parse(employeeRequest.getStartDate()))
                    .title(Title.builder()
                            .titleId(titleRepository.fetchTitleId(employeeRequest.getTitle()))
                            .name(employeeRequest.getTitle())
                            .build())
                    .department(Department.builder()
                            .departmentId(departmentRepository.fetchDepartmentId(employeeRequest.getDepartment()))
                            .name(employeeRequest.getDepartment())
                            .build())
                    .build());
        } catch (Exception ex) {
            ex.printStackTrace();
            Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.ok("Successfully added employee").build();
    }

}
