package com.employee.service;

import com.employee.model.Employee;
import com.employee.model.Role;
import com.employee.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserPermissionService {

    public boolean canViewEmployee(Employee employee) {
        User currentUser = getCurrentUser();
        return switch (currentUser.getRole()) {
            case HR_PERSONNEL, ADMINISTRATOR -> true;
            case MANAGER -> employee.getDepartment().getId().equals(currentUser.getDepartment().getId());
        };
    }

    public boolean canEditEmployee(Employee employee) {
        User currentUser = getCurrentUser();
        return switch (currentUser.getRole()) {
            case HR_PERSONNEL, ADMINISTRATOR -> true;
            case MANAGER -> employee.getDepartment().getId().equals(currentUser.getDepartment().getId());
        };
    }

    public boolean canDeleteEmployee(Employee employee) {
        User currentUser = getCurrentUser();
        return currentUser.getRole() == Role.HR_PERSONNEL || 
               currentUser.getRole() == Role.ADMINISTRATOR;
    }

    public boolean canManageUsers() {
        User currentUser = getCurrentUser();
        return currentUser.getRole() == Role.ADMINISTRATOR;
    }

    public boolean canViewDepartment(Long departmentId) {
        User currentUser = getCurrentUser();
        return switch (currentUser.getRole()) {
            case HR_PERSONNEL, ADMINISTRATOR -> true;
            case MANAGER -> currentUser.getDepartment().getId().equals(departmentId);
        };
    }

    public boolean canGenerateReports() {
        User currentUser = getCurrentUser();
        return currentUser.getRole() == Role.HR_PERSONNEL || 
               currentUser.getRole() == Role.ADMINISTRATOR ||
               currentUser.getRole() == Role.MANAGER;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
