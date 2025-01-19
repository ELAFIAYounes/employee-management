-- Clear existing data
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM employees;
DELETE FROM departments;

-- Insert departments
INSERT INTO departments (id, name, description) 
VALUES (1, 'Engineering', 'Software Development and Engineering');

INSERT INTO departments (id, name, description) 
VALUES (2, 'HR', 'Human Resources Management');

INSERT INTO departments (id, name, description) 
VALUES (3, 'Finance', 'Financial Management and Planning');

-- Insert employees
INSERT INTO employees (employee_id, full_name, job_title, department_id, hire_date, status, email, phone, address) 
VALUES ('EMP001', 'John Doe', 'Software Engineer', 1, TO_DATE('2025-01-19', 'YYYY-MM-DD'), 'ACTIVE', 'john.doe@company.com', '+1234567890', '123 Main St, City');

INSERT INTO employees (employee_id, full_name, job_title, department_id, hire_date, status, email, phone, address) 
VALUES ('EMP002', 'Jane Smith', 'HR Manager', 2, TO_DATE('2025-01-19', 'YYYY-MM-DD'), 'ACTIVE', 'jane.smith@company.com', '+1234567891', '456 Oak St, City');

INSERT INTO employees (employee_id, full_name, job_title, department_id, hire_date, status, email, phone, address) 
VALUES ('EMP003', 'Bob Wilson', 'Financial Analyst', 3, TO_DATE('2025-01-19', 'YYYY-MM-DD'), 'ACTIVE', 'bob.wilson@company.com', '+1234567892', '789 Pine St, City');

-- Insert admin user with raw password 'admin123'
-- Note: This is a verified working BCrypt hash for 'admin123'
INSERT INTO users (id, username, password, enabled) 
VALUES (
    1, 
    'admin',
    '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS',
    1
);

-- Insert admin role
INSERT INTO user_roles (user_id, role) VALUES (1, 'ADMIN');
