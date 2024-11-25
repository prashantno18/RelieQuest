package com.reliaquest.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reliaquest.api.wrapper.EmployeeRequest;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Employee {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("employee_name")
    private String employeeName;

    @JsonProperty("employee_salary")
    private int employeeSalary;

    @JsonProperty("employee_age")
    private int employeeAge;

    @JsonProperty("employee_title")
    private String employeeTitle;
    @JsonProperty("employee_email")
    private String employeeEmail;


    public Employee(String name, int salary, int age, String title) {
        this.employeeName = name;
        this.employeeSalary = salary;
        this.employeeAge = age;
        this.employeeTitle = title;
    }

    public EmployeeRequest toEmployeeRequest() {
        return new EmployeeRequest(employeeName, employeeSalary, employeeAge, employeeTitle);
    }
}
