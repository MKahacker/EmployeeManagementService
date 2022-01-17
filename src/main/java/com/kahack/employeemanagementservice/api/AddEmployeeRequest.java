package com.kahack.employeemanagementservice.api;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class AddEmployeeRequest {
    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String startDate;

    @NonNull
    private String department;

    @NonNull
    private String title;
}
