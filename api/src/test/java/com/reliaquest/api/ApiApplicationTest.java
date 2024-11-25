package com.reliaquest.api;

import com.reliaquest.api.controller.EmployeeController;
import com.reliaquest.api.exception.CustomException;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.IEmployeeService;
import com.reliaquest.api.wrapper.EmployeeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ApiApplicationTest {

    @Mock
    private IEmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> mockEmployees = Arrays.asList(
                new Employee(UUID.fromString("e5c15c5a-5056-46f4-81fa-993c6a892acf"), "Ashish", 50000, 30, "Engineer", "Ashish@example.com")
        );
        when(employeeService.getAllEmployees()).thenReturn(mockEmployees);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();
        assertEquals(1, response.getBody().size());
        assertEquals("Ashish", response.getBody().get(0).getEmployeeName());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testGetAllEmployees_Exception() {
        when(employeeService.getAllEmployees()).thenThrow(new RuntimeException("Error fetching employees"));

        CustomException exception = assertThrows(CustomException.class, () -> employeeController.getAllEmployees());
        assertEquals("Could not fetch all employees.", exception.getMessage());
    }

    @Test
    void testGetEmployeesByNameSearch() {
        String searchString = "Ashish";
        List<Employee> mockEmployees = List.of(new Employee(UUID.fromString("e5c15c5a-5056-46f4-81fa-993c6a892acf"), "Ashish", 50000, 30, "Engineer", "Ashish@example.com"));
        when(employeeService.getEmployeesByNameSearch(searchString)).thenReturn(mockEmployees);

        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch(searchString);
        assertEquals(1, response.getBody().size());
        assertEquals("Ashish", response.getBody().get(0).getEmployeeName());
        verify(employeeService, times(1)).getEmployeesByNameSearch(searchString);
    }

    @Test
    void testGetEmployeesByNameSearch_Exception() {
        String searchString = "Ashish";
        when(employeeService.getEmployeesByNameSearch(searchString)).thenThrow(new RuntimeException("Error fetching employees"));

        CustomException exception = assertThrows(CustomException.class, () -> employeeController.getEmployeesByNameSearch(searchString));
        assertEquals("Could not fetch employees by name search.", exception.getMessage());
    }

    @Test
    void testGetEmployeeById() {
        String id = "1";
        Employee mockEmployee = new Employee(UUID.fromString("e5c15c5a-5056-46f4-81fa-993c6a892acf"), "Ashish", 50000, 30, "Engineer", "Ashish@example.com");
        when(employeeService.getEmployeeById(id)).thenReturn(mockEmployee);

        ResponseEntity<Employee> response = employeeController.getEmployeeById(id);
        assertEquals("Ashish", response.getBody().getEmployeeName());
        verify(employeeService, times(1)).getEmployeeById(id);
    }

    @Test
    void testGetEmployeeById_Exception() {
        String id = "1";
        when(employeeService.getEmployeeById(id)).thenThrow(new RuntimeException("Error fetching employee"));

        CustomException exception = assertThrows(CustomException.class, () -> employeeController.getEmployeeById(id));
        assertEquals("Could not fetch employee by ID: " + id, exception.getMessage());
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
        int highestSalary = 60000;
        when(employeeService.getHighestSalaryOfEmployees()).thenReturn(highestSalary);

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();
        assertEquals(60000, response.getBody());
        verify(employeeService, times(1)).getHighestSalaryOfEmployees();
    }

    @Test
    void testGetHighestSalaryOfEmployees_Exception() {
        when(employeeService.getHighestSalaryOfEmployees()).thenThrow(new RuntimeException("Error fetching highest salary"));

        CustomException exception = assertThrows(CustomException.class, () -> employeeController.getHighestSalaryOfEmployees());
        assertEquals("Could not fetch the highest salary.", exception.getMessage());
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        List<String> mockEmployeeNames = Arrays.asList("Ashish", "Prashant", "Prayag");
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(mockEmployeeNames);

        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();
        assertEquals(3, response.getBody().size());
        assertEquals("Ashish", response.getBody().get(0));
        verify(employeeService, times(1)).getTopTenHighestEarningEmployeeNames();
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames_Exception() {
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenThrow(new RuntimeException("Error fetching employee names"));

        CustomException exception = assertThrows(CustomException.class, () -> employeeController.getTopTenHighestEarningEmployeeNames());
        assertEquals("Could not fetch the top 10 highest-earning employee names.", exception.getMessage());
    }

    @Test
    void testCreateEmployee() {
        EmployeeRequest request = new EmployeeRequest("Ashish", 50000, 30, "Engineer");
        Employee mockEmployee = new Employee(UUID.fromString("e5c15c5a-5056-46f4-81fa-993c6a892acf"), "Ashish", 50000, 30, "Engineer", "Ashish@example.com");
        when(employeeService.createEmployee(request)).thenReturn(mockEmployee);

        ResponseEntity<Employee> response = employeeController.createEmployee(request);
        assertEquals("Ashish", response.getBody().getEmployeeName());
        verify(employeeService, times(1)).createEmployee(request);
    }

    @Test
    void testCreateEmployee_Exception() {
        EmployeeRequest request = new EmployeeRequest("Ashish", 50000, 1, "Engineer");
        when(employeeService.createEmployee(request)).thenThrow(new RuntimeException("Error creating employee"));

        CustomException exception = assertThrows(CustomException.class, () -> employeeController.createEmployee(request));
        assertEquals("Could not create the employee.", exception.getMessage());
    }

    @Test
    void testDeleteEmployeeById() {
        String id = "1";
        String mockEmployeeName = "Ashish";
        when(employeeService.deleteEmployeeById(id)).thenReturn(mockEmployeeName);

        ResponseEntity<String> response = employeeController.deleteEmployeeById(id);
        assertEquals("Employee deleted: Ashish", response.getBody());
        verify(employeeService, times(1)).deleteEmployeeById(id);
    }

    @Test
    void testDeleteEmployeeById_Exception() {
        String id = "1";
        when(employeeService.deleteEmployeeById(id)).thenThrow(new RuntimeException("Error deleting employee"));

        CustomException exception = assertThrows(CustomException.class, () -> employeeController.deleteEmployeeById(id));
        assertEquals("Could not delete employee with ID: " + id, exception.getMessage());
    }

    @Test
    void testDeleteEmployeeByName() {
        String mockEmployeeName = "Ashish";
        when(employeeService.deleteEmployeeByName(mockEmployeeName)).thenReturn(Boolean.TRUE);

        ResponseEntity<Boolean> response = employeeController.deleteEmployeeByName(mockEmployeeName);
        assertEquals(Boolean.TRUE, response.getBody());
        verify(employeeService, times(1)).deleteEmployeeByName(mockEmployeeName);
    }

    @Test
    void testDeleteEmployeeByName_Exception() {
        String mockEmployeeName = "Ashish";
        when(employeeService.deleteEmployeeByName(mockEmployeeName)).thenThrow(new RuntimeException("Error deleting employee"));
        CustomException exception = assertThrows(CustomException.class, () -> employeeController.deleteEmployeeByName(mockEmployeeName));
        assertEquals("Could not delete employee with Name: " + mockEmployeeName, exception.getMessage());
    }
}