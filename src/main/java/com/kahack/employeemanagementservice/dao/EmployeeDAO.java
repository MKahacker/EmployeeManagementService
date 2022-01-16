package com.kahack.employeemanagementservice.dao;

import com.kahack.employeemanagementservice.core.Employee;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.joda.time.DateTime;

import java.util.List;

public interface EmployeeDAO {
  @SqlQuery("INSERT INTO employee (id, first_name, last_name, title_id, start_date, department_id) values (:id, :firstName, :lastName, :titleId, :startDate, :departmentId")
  void insertEmployee(@Bind("id") String id,
                      @Bind("firstName") String firstName,
                      @Bind("lastName") String lastName,
                      @Bind("titleId") String title_id,
                      @Bind("startDate") DateTime startDate,
                      @Bind("departmentId")String department_id);

  @SqlQuery("SELECT employee_id, first_name, last_name, title_id, start_date, department_id FROM employee WHERE department_id=:departmentId")
  List<Employee> findEmployeesByDepartment(@Bind("departmentId") String department_id);
}
