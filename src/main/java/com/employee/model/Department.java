package com.employee.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Department name is required")
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "department")
    private Set<Employee> employees;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;
}
