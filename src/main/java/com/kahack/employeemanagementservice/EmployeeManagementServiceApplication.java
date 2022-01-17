package com.kahack.employeemanagementservice;

import com.kahack.employeemanagementservice.repository.JdbiDepartmentRepository;
import com.kahack.employeemanagementservice.repository.JdbiEmployeeRepository;
import com.kahack.employeemanagementservice.repository.JdbiTitleRepository;
import com.kahack.employeemanagementservice.resources.EmployeeResource;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

public class EmployeeManagementServiceApplication extends Application<EmployeeManagementServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new EmployeeManagementServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "EmployeeManagementService";
    }

    @Override
    public void initialize(final Bootstrap<EmployeeManagementServiceConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final EmployeeManagementServiceConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        environment.jersey().register(
                EmployeeResource.builder()
                        .employeeRepository(JdbiEmployeeRepository.builder().jdbi(jdbi).build())
                        .titleRepository(JdbiTitleRepository.builder().jdbi(jdbi).build())
                        .departmentRepository(JdbiDepartmentRepository.builder().jdbi(jdbi).build())
                        .build());
    }

}
