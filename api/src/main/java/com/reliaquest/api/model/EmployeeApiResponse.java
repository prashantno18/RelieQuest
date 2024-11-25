package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeApiResponse {

    @JsonProperty("data")
    private List<Employee> data;

    @JsonProperty("status")
    private String status;
}
