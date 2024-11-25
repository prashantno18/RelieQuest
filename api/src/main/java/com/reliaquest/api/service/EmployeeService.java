package com.reliaquest.api.service;

import com.reliaquest.api.exception.CustomException;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeApiResponse;
import com.reliaquest.api.repository.EmployeeRepository;
import com.reliaquest.api.wrapper.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService implements IEmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        logger.info("Service: Fetching all employees");
        try {
            return repository.fetchAllEmployees();
        } catch (Exception e) {
            logger.error("Error fetching all employees: {}", e.getMessage());
            throw new CustomException("Could not fetch all employees.");
        }
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        logger.info("Service: Fetching employees by name search: {}", searchString);
        try {
            return repository.fetchAllEmployees().stream()
                    .filter(employee -> employee.getEmployeeName().toLowerCase().contains(searchString.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching employees by name search: {}", searchString, e);
            throw new CustomException("Could not fetch employees by name search.");
        }
    }

    @Override
    public Employee getEmployeeById(String id) {
        logger.info("Service: Fetching employee by ID: {}", id);
        try {
            return repository.fetchEmployeeById(id);
        } catch (Exception e) {
            logger.error("Error fetching employee by ID: {}", id, e);
            throw new CustomException("Could not fetch employee by ID: " + id);
        }
    }

    @Override
    public int getHighestSalaryOfEmployees() {
        logger.info("Service: Fetching the highest salary of employees");
        try {
            return repository.fetchAllEmployees().stream()
                    .mapToInt(Employee::getEmployeeSalary)
                    .max()
                    .orElse(0);
        } catch (Exception e) {
            logger.error("Error fetching the highest salary: {}", e.getMessage());
            throw new CustomException("Could not fetch the highest salary.");
        }
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        logger.info("Service: Fetching the top 10 highest-earning employee names");
        try {
            return repository.fetchAllEmployees().stream()
                    .sorted((e1, e2) -> e2.getEmployeeSalary() - e1.getEmployeeSalary())
                    .limit(10)
                    .map(Employee::getEmployeeName)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching the top 10 highest-earning employee names: {}", e.getMessage());
            throw new CustomException("Could not fetch the top 10 highest-earning employee names.");
        }
    }

    @Override
    public Employee createEmployee(EmployeeRequest employeeRequest) {
        logger.info("Service: Creating employee: {}", employeeRequest);
        try {
            return repository.createEmployee(employeeRequest);
        } catch (Exception e) {
            logger.error("Error creating employee: {}", employeeRequest, e);
            throw new CustomException("Could not create the employee.");
        }
    }

    @Override
    public String deleteEmployeeById(String id) {
        logger.info("Service: Deleting employee by ID: {}", id);
        try {
            Employee employee = getEmployeeById(id);
            boolean deleted = repository.deleteEmployeeById(id);
            if (deleted) {
                logger.info("Successfully deleted employee: {}", employee.getEmployeeName());
                return employee.getEmployeeName();
            } else {
                throw new CustomException("Failed to delete employee with ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Error deleting employee by ID: {}", id, e);
            throw new CustomException("Could not delete employee with ID: " + id);
        }
    }

    @Override
    public boolean deleteEmployeeByName(String employeeName) {
        logger.info("Service: Delete employee by name: {}", employeeName);
        try {
            return repository.deleteEmployeeByName(employeeName);
        } catch (Exception e) {
            logger.error("Error deleting employee by it's name: {}", employeeName, e);
            throw new CustomException("Could not deleted the employee by its name.");
        }
    }
}