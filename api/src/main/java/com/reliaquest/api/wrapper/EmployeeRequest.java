package com.reliaquest.api.wrapper;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeRequest {

    private String name;

    private int salary;

    private int age;

    private String title;
}
