package com.kahack.employeemanagementservice.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @JsonIgnore
    private String departmentId;
    private String name;
}
