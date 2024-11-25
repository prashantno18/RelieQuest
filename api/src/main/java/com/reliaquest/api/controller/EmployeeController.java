package com.reliaquest.api.controller;

import com.reliaquest.api.exception.CustomException;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.IEmployeeService;
import com.reliaquest.api.wrapper.EmployeeRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController implements IEmployeeController<Employee, Employee> {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
    private final IEmployeeService employeeService;

    @GetMapping
    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("Fetching all employees.");
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            log.info("Successfully fetched all employees : {}", employees.size());
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error fetching all employees.", e);
            throw new CustomException("Could not fetch all employees.");
        }
    }

    @GetMapping("/search/{searchString}")
    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        log.info("Fetching employees by name search: {}", searchString);
        try {
            List<Employee> employees = employeeService.getEmployeesByNameSearch(searchString);
            log.info("Successfully fetched employees by name search.");
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            log.error("Error fetching employees by name search: {}", searchString, e);
            throw new CustomException("Could not fetch employees by name search.");
        }
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        log.info("Fetching employee by ID: {}", id);
        try {
            Employee employee = employeeService.getEmployeeById(id);
            log.info("Successfully fetched employee by ID: {}", id);
            return ResponseEntity.ok(employee);
        } catch (Exception e) {
            log.error("Error fetching employee by ID: {}", id, e);
            throw new CustomException("Could not fetch employee by ID: " + id);
        }
    }

    @Override
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        log.info("Fetching the highest salary of employees.");
        try {
            int highestSalary = employeeService.getHighestSalaryOfEmployees();
            log.info("Successfully fetched the highest salary: {}", highestSalary);
            return ResponseEntity.ok(highestSalary);
        } catch (Exception e) {
            log.error("Error fetching the highest salary.", e);
            throw new CustomException("Could not fetch the highest salary.");
        }
    }

    @Override
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        log.info("Fetching the top 10 highest-earning employee names.");
        try {
            List<String> employeeNames = employeeService.getTopTenHighestEarningEmployeeNames();
            log.info("Successfully fetched the top 10 highest-earning employee names.");
            return ResponseEntity.ok(employeeNames);
        } catch (Exception e) {
            log.error("Error fetching the top 10 highest-earning employee names.", e);
            throw new CustomException("Could not fetch the top 10 highest-earning employee names.");
        }
    }

    @Override
    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeRequest employeeInput) {
        log.info("Creating a new employee: {}", employeeInput);
        try {
            Employee createdEmployee = employeeService.createEmployee(employeeInput);
            log.info("Successfully created employee: {}", createdEmployee);
            return ResponseEntity.ok(createdEmployee);
        } catch (Exception e) {
            log.error("Error creating employee: {}", employeeInput, e);
            throw new CustomException("Could not create the employee.");
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        log.info("Deleting employee by ID: {}", id);
        try {
            String deletedEmployeeName = employeeService.deleteEmployeeById(id);
            log.info("Successfully deleted employee: {}", deletedEmployeeName);
            return ResponseEntity.ok("Employee deleted: " + deletedEmployeeName);
        } catch (Exception e) {
            log.error("Error deleting employee by ID: {}", id, e);
            throw new CustomException("Could not delete employee with ID: " + id);
        }
    }

    @Override
    @DeleteMapping("/name/{employeeName}")
    public ResponseEntity<Boolean> deleteEmployeeByName(@PathVariable String employeeName) {
        log.info("Deleting employee by ID: {}", employeeName);
        try {
            Boolean deletedEmployeeByName = employeeService.deleteEmployeeByName(employeeName);
            log.info("Successfully deleted employee: {}", deletedEmployeeByName);
            return ResponseEntity.ok(deletedEmployeeByName);
        } catch (Exception e) {
            log.error("Error deleting employee: {}", employeeName, e);
            throw new CustomException("Could not delete employee with Name: " + employeeName);
        }
    }
}
