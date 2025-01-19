package com.employee.controller;

import com.employee.model.Employee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Employee", description = "Employee management API")
@RequestMapping("/api/v1/employees")
public interface EmployeeController {

    @Operation(summary = "Get all employees", description = "Retrieves a list of all employees")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of employees",
                    content = @Content(schema = @Schema(implementation = Employee.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    ResponseEntity<List<Employee>> getAllEmployees();

    @Operation(summary = "Get an employee by ID", description = "Retrieves an employee by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the employee"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    ResponseEntity<Employee> getEmployeeById(
        @Parameter(description = "ID of the employee to retrieve") @PathVariable Long id);

    @Operation(summary = "Create a new employee", description = "Creates a new employee record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Employee successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    ResponseEntity<Employee> createEmployee(
        @Parameter(description = "Employee to create") @Valid @RequestBody Employee employee);

    @Operation(summary = "Update an employee", description = "Updates an existing employee record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee successfully updated"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    ResponseEntity<Employee> updateEmployee(
        @Parameter(description = "ID of the employee to update") @PathVariable Long id,
        @Parameter(description = "Updated employee details") @Valid @RequestBody Employee employee);

    @Operation(summary = "Delete an employee", description = "Deletes an employee record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Employee successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEmployee(
        @Parameter(description = "ID of the employee to delete") @PathVariable Long id);
}
