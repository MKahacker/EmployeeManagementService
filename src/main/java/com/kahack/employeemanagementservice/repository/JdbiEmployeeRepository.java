package com.kahack.employeemanagementservice.repository;

import com.kahack.employeemanagementservice.core.Employee;
import com.kahack.employeemanagementservice.dao.EmployeeMapper;
import lombok.Builder;
import org.jdbi.v3.core.Jdbi;


import java.util.List;
import java.util.Optional;

import static com.kahack.employeemanagementservice.util.DateUtil.formatter;

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
    private static final String ADD_EMPLOYEE_TO_TABLE = "INSERT INTO employee(employee_id, first_name, last_name, start_date, department_id, title_id) " +
            "VALUES(UUID_TO_BIN(UUID()), ?, ?, ?, UUID_TO_BIN(?), UUID_TO_BIN(?))";
    private static final String FIND_DUPLICATE_EMPLOYEE = "SELECT * FROM employee WHERE first_name=:first_name AND last_name=:last_name AND start_date=:start_date";
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
        System.out.println(String.format("Adding employee: %s, %s, %s, %s, %s to database",
                employee.getFirstName(),
                employee.getLastName(),
                employee.getStartDate(),
                employee.getDepartment().getName(),
                employee.getTitle().getName()));

        jdbi.withHandle(handle -> handle.execute(ADD_EMPLOYEE_TO_TABLE,
                employee.getFirstName(),
                employee.getLastName(),
                formatter.format(employee.getStartDate()),
                employee.getDepartment().getDepartmentId(),
                employee.getTitle().getTitleId()));
    }

    public boolean duplicateEmployee(final Employee employee) {
        Optional<Employee> duplicateEmployee = jdbi.withHandle(handle -> handle
                .createQuery(FIND_DUPLICATE_EMPLOYEE)
                .bind("first_name", employee.getFirstName())
                .bind("last_name", employee.getLastName())
                .bind("start_date", formatter.format(employee.getStartDate()))
                .mapToBean(Employee.class)
                .findFirst());

        if (duplicateEmployee.isPresent()) {
            return true;
        }

        return false;
    }
}
