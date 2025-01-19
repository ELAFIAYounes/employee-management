package com.employee.service;

import com.employee.model.Employee;
import com.employee.model.EmploymentStatus;
import com.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setEmployeeId("EMP001");
        testEmployee.setFullName("John Doe");
        testEmployee.setJobTitle("Software Engineer");
        testEmployee.setHireDate(LocalDate.now());
        testEmployee.setStatus(EmploymentStatus.ACTIVE);
        testEmployee.setEmail("john.doe@company.com");
    }

    @Test
    void createEmployee_Success() {
        when(employeeRepository.existsByEmployeeId(anyString())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        Employee created = employeeService.createEmployee(testEmployee);

        assertNotNull(created);
        assertEquals("EMP001", created.getEmployeeId());
        verify(auditService).logEvent(eq("CREATE"), anyString());
    }

    @Test
    void createEmployee_DuplicateId() {
        when(employeeRepository.existsByEmployeeId(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            employeeService.createEmployee(testEmployee);
        });

        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void getEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        Employee found = employeeService.getEmployee(1L);

        assertNotNull(found);
        assertEquals("John Doe", found.getFullName());
    }

    @Test
    void updateEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);

        Employee updated = employeeService.updateEmployee(1L, testEmployee);

        assertNotNull(updated);
        verify(auditService).logEvent(eq("UPDATE"), anyString());
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(testEmployee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).delete(testEmployee);
        verify(auditService).logEvent(eq("DELETE"), anyString());
    }
}
