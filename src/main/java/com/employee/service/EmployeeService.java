package com.employee.service;

import com.employee.model.Employee;
import com.employee.model.EmploymentStatus;
import com.employee.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AuditService auditService;

    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    public List<Employee> getAllEmployees() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auditService.logAction("VIEW_ALL", "Employee", "User " + auth.getName() + " viewed all employees");
        return employeeRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    public List<Employee> searchEmployees(String query) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auditService.logAction("SEARCH", "Employee", "User " + auth.getName() + " searched for employees with query: " + query);
        if (query == null || query.trim().isEmpty()) {
            return getAllEmployees();
        }
        return employeeRepository.findByFullNameContainingIgnoreCase(query);
    }

    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    @Transactional
    public Employee createEmployee(Employee employee) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (employeeRepository.existsByEmployeeId(employee.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists");
        }
        Employee savedEmployee = employeeRepository.save(employee);
        auditService.logAction("CREATE", "Employee", "User " + auth.getName() + " created employee: " + employee.getEmployeeId());
        return savedEmployee;
    }

    @PreAuthorize("hasAnyRole('HR', 'ADMIN')")
    @Transactional
    public Employee updateEmployee(Long id, Employee employee) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employee existingEmployee = getEmployee(id);
        // Update fields
        existingEmployee.setFullName(employee.getFullName());
        existingEmployee.setJobTitle(employee.getJobTitle());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setStatus(employee.getStatus());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPhone(employee.getPhone());
        existingEmployee.setAddress(employee.getAddress());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        auditService.logAction("UPDATE", "Employee", "User " + auth.getName() + " updated employee: " + employee.getEmployeeId());
        return updatedEmployee;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteEmployee(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = getEmployee(id);
        employeeRepository.delete(employee);
        auditService.logAction("DELETE", "Employee", "User " + auth.getName() + " deleted employee with id: " + id);
    }

    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    public Employee getEmployee(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auditService.logAction("VIEW", "Employee", "User " + auth.getName() + " viewed employee with id: " + id);
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
    }

    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    public List<Employee> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    public List<Employee> getEmployeesByStatus(EmploymentStatus status) {
        return employeeRepository.findByStatus(status);
    }

    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'MANAGER')")
    public List<Employee> getEmployeesByHireDateRange(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.findByHireDateBetween(startDate, endDate);
    }
}
