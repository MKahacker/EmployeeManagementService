package com.kahack.employeemanagementservice.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Title {
    @JsonIgnore
    private String titleId;
    private String name;
}
