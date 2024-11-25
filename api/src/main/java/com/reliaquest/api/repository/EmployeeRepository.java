package com.reliaquest.api.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.exception.CustomException;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeApiResponse;
import com.reliaquest.api.utils.Constants;
import com.reliaquest.api.wrapper.EmployeeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
public class EmployeeRepository {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeRepository.class);
    private final RestTemplate restTemplate;

    public EmployeeRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Employee> fetchAllEmployees() {
        logger.info("Fetching all employees from {}", Constants.BASE_URL);
        try {
            EmployeeApiResponse response = restTemplate.getForObject(Constants.BASE_URL, EmployeeApiResponse.class);
            if (response != null && response.getData() != null) {
                logger.info("Successfully fetched {} employees", response.getData().size());
                return response.getData();
            } else {
                logger.warn("No data found in API response");
                return List.of();
            }
        } catch (Exception e) {
            logger.error("Error fetching all employees: {}", e.getMessage());
            throw new CustomException("Error fetching all employees");
        }
    }

    public Employee fetchEmployeeById(String id) {
        String url = Constants.BASE_URL + "/" + id;
        logger.info("Fetching employee by ID: {}: {}", id, url);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            logger.info("Raw response: {}", response.getBody());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode dataNode = rootNode.path("data");
            Employee employee = objectMapper.treeToValue(dataNode, Employee.class);
            if (employee != null) {
                logger.info("Employee fetched: {}", employee);
            } else {
                logger.warn("Employee object is null");
            }
            return employee;
        } catch (Exception e) {
            logger.error("Error fetching employee with ID {}: {}", id, e.getMessage());
            throw new CustomException("Employee not found with ID: " + id);
        }
    }

    public Employee createEmployee(EmployeeRequest employee) {
        String url = Constants.BASE_URL_STRING;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EmployeeRequest> entity = new HttpEntity<>(employee, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode dataNode = rootNode.path("data");
            Employee finalResponse = objectMapper.treeToValue(dataNode, Employee.class);
            if (response.getStatusCode() != HttpStatus.OK) {
                throw new CustomException("Error creating employee");
            }
            return finalResponse;
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error occurred while creating employee: {}", e.getMessage());
            throw new CustomException("HTTP error occurred while creating employee");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating employee: {}", e.getMessage());
            throw new CustomException("Unexpected error occurred while creating employee");
        }
    }

    public boolean deleteEmployeeById(String id) {
        String url = Constants.BASE_URL + "/" + id;
        logger.info("Deleting employee by ID: {}", id);
        try {
            restTemplate.delete(url);
            logger.info("Successfully deleted employee with ID: {}", id);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            logger.error("Employee not found with ID: {}", id, e);
            throw new CustomException("Employee not found with ID: " + id);
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error occurred while deleting employee with ID: {}", id, e);
            throw new CustomException("HTTP error occurred while deleting employee with ID: " + id);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting employee with ID: {}", id, e);
            throw new CustomException("Unexpected error occurred while deleting employee with ID: " + id);
        }
    }

    public boolean deleteEmployeeByName(String employeeName) {
        logger.info("Deleting employee by name: {}", employeeName);
        String url = Constants.BASE_URL_STRING;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a JSON object with the employee name
        String jsonPayload = "{\"name\":\"" + employeeName + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
        logger.info("entity: {}", entity);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
            logger.info("Raw response: {}", response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                // Parse the response body to Boolean
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                boolean result = rootNode.get("data").asBoolean();
                return result;
            } else {
                throw new CustomException("Error deleting employee by name");
            }
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error occurred while deleting employee by name: {}", e.getMessage());
            throw new CustomException("HTTP error occurred while deleting employee by name");
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting employee by name: {}", e.getMessage());
            throw new CustomException("Unexpected error occurred while deleting employee by name");
        }
    }
}