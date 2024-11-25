package com.reliaquest.api.service;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeApiResponse;
import com.reliaquest.api.wrapper.EmployeeRequest;


import java.util.List;

public interface IEmployeeService {

    List<Employee> getAllEmployees();
    List<Employee> getEmployeesByNameSearch(String searchString);
    Employee getEmployeeById(String id);
    int getHighestSalaryOfEmployees();
    List<String> getTopTenHighestEarningEmployeeNames();

    Employee createEmployee(EmployeeRequest employeeInput);

    String deleteEmployeeById(String id);

    boolean deleteEmployeeByName(String employeeInput);
}
