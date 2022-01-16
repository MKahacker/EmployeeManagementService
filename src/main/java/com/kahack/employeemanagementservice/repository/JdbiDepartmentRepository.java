package com.kahack.employeemanagementservice.repository;

import com.kahack.employeemanagementservice.core.Department;
import lombok.Builder;
import org.jdbi.v3.core.Jdbi;

import java.util.Optional;

@Builder
public class JdbiDepartmentRepository implements DepartmentRepository {
    private final Jdbi jdbi;

    @Override
    public String fetchDepartmentId(String departmentName) {
        Optional<Department> department = findDepartmentResourceByName(departmentName);

        if(department.isPresent()) {
            System.out.println(String.format("Found department: %s, %s", department.get().getName(),department.get().getDepartmentId()));
            return department.get().getDepartmentId();
        }

        System.out.println(String.format("Creating new department: %s", departmentName));
        String departmentId = generateUUID();
        addDepartment(Department.builder().departmentId(departmentId).name(departmentName).build());

        return departmentId;
    }

    private String generateUUID() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT UUID()").mapTo(String.class).first());
    }

    @Override
    public Optional<Department> findDepartmentResourceByName(String departmentName) {
        System.out.println("Finding department name with string");
        System.out.println(departmentName);
        return jdbi.withHandle(handle -> handle.createQuery("SELECT BIN_TO_UUID(department_id) AS department_id, name FROM department WHERE name=:name")
                .bind("name", departmentName)
                .mapToBean(Department.class)
                .findFirst());
    }

    @Override
    public void addDepartment(Department department) {
        jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO department(department_id, name) VALUES(UUID_TO_BIN(?),?)")
                .bind(0, department.getDepartmentId())
                .bind(1, department.getName())
                .execute());
    }
}
