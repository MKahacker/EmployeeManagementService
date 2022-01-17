package com.kahack.employeemanagementservice.dao;

import com.kahack.employeemanagementservice.core.Employee;
import com.kahack.employeemanagementservice.core.Department;
import com.kahack.employeemanagementservice.core.Title;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import static com.kahack.employeemanagementservice.util.DateUtil.formatter;


public class EmployeeMapper implements RowMapper<Employee> {
        @Override
        public Employee map(ResultSet r, StatementContext ctx) throws SQLException {
            try {
                return Employee.builder()
                        .firstName(r.getString("firstName"))
                        .lastName(r.getString("lastName"))
                        .startDate(formatter.parse(r.getString("startDate")))
                        .title(Title.builder()
                                .name(r.getString("titleName"))
                                .build())
                        .department(Department.builder()
                                .name(r.getString("departmentName"))
                                .build())
                        .build();
            } catch (ParseException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
}

