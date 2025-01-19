package com.employee.repository;

import com.employee.model.Employee;
import com.employee.model.EmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    List<Employee> findByDepartmentId(Long departmentId);
    List<Employee> findByStatus(EmploymentStatus status);
    List<Employee> findByHireDateBetween(LocalDate startDate, LocalDate endDate);
    List<Employee> findByFullNameContainingIgnoreCase(String name);
    boolean existsByEmployeeId(String employeeId);
}
