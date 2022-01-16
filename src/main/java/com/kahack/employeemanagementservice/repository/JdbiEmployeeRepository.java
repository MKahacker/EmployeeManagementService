package com.kahack.employeemanagementservice.repository;

import com.kahack.employeemanagementservice.core.Employee;
import com.kahack.employeemanagementservice.dao.EmployeeMapper;
import lombok.Builder;
import org.jdbi.v3.core.Jdbi;


import java.util.List;

@Builder
public class JdbiEmployeeRepository implements EmployeeRepository{
    private static final String FIND_EMPLOYEES_IN_DEPARTMENT_STATEMENT = "SELECT BIN_TO_UUID(employee_id) AS employee_id, " +
            "e.first_name AS firstName, " +
            "e.last_name AS lastName, " +
            "t.name AS titleName, " +
            "t.title_id AS titleId, " +
            "Date(e.start_date) AS startDate, " +
            "d.name AS departmentName, " +
            "d.department_id AS departmentId " +
            "FROM employee AS e " +
            "JOIN department AS d " +
            "ON e.department_id=d.department_id " +
            "JOIN title AS t " +
            "ON e.title_id=t.title_id " +
            "WHERE BIN_TO_UUID(d.department_id)=:department_id";
//    private static final String ADD_EMPLOYEE_TO_TABLE = "INSERT INTO employee(employee_id, first_name, last_name, start_date, department_id, title_id) " +
//            "VALUES(UUID_TO_BIN(UUID()), :first_name, :last_name, :start_date, UUID_TO_BIN(:department_id), UUID_TO_BIN(:title_id))";
    private static final String ADD_EMPLOYEE_TO_TABLE = "INSERT INTO employee(employee_id, first_name, last_name, start_date, department_id, title_id) " +
            "VALUES(UUID_TO_BIN(UUID()), ?, ?, ?, UUID_TO_BIN(?), UUID_TO_BIN(?))";
    private final Jdbi jdbi;

    @Override
    public List<Employee> getEmployeesByDepartment(final String departmentId) {
        return jdbi.withHandle(handle -> handle.createQuery(FIND_EMPLOYEES_IN_DEPARTMENT_STATEMENT)
                .bind("department_id", departmentId)
                .map(new EmployeeMapper())
                .list());
    }

    @Override
    public void addEmployee(final Employee employee) {
        System.out.println("-------------------- employee.getFirstName() ------------------");
        System.out.println(employee.getFirstName());
        System.out.println(employee.getLastName());
        System.out.println(employee.getStartDate());
        System.out.println(employee.getDepartment().getName());
        System.out.println(employee.getDepartment().getDepartmentId());
        System.out.println(employee.getTitle().getTitleId());
        System.out.println(employee.getTitle().getName());
        jdbi.withHandle(handle -> handle.execute(ADD_EMPLOYEE_TO_TABLE,
                employee.getFirstName(),
                employee.getLastName(),
                employee.getStartDate(),
                employee.getDepartment().getDepartmentId(),
                employee.getTitle().getTitleId()));
//        jdbi.withHandle(handle -> handle.createUpdate(ADD_EMPLOYEE_TO_TABLE)
//                .bind("first_name", employee.getFirstName())
//                .bind("last_name", employee.getLastName())
//                .bind("start_date", employee.getStartDate())
//                .bind("department_id", employee.getDepartment().getDepartmentId())
//                .bind("title_id", employee.getTitle().getTitleId())
//                .execute();
    }
}
