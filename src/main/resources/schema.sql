-- Drop existing tables if they exist
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE audit_logs';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE user_roles';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE users';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE employees';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE departments';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
      END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE employee_seq';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2289 THEN
         RAISE;
      END IF;
END;
/

-- Create sequences
CREATE SEQUENCE employee_seq START WITH 1 INCREMENT BY 1;

-- Create tables
CREATE TABLE departments (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(100) NOT NULL,
    description VARCHAR2(500)
);

CREATE TABLE employees (
    id NUMBER DEFAULT employee_seq.NEXTVAL PRIMARY KEY,
    employee_id VARCHAR2(50) NOT NULL UNIQUE,
    full_name VARCHAR2(100) NOT NULL,
    job_title VARCHAR2(100) NOT NULL,
    department_id NUMBER,
    hire_date DATE NOT NULL,
    status VARCHAR2(20) NOT NULL,
    email VARCHAR2(100) NOT NULL UNIQUE,
    phone VARCHAR2(20),
    address VARCHAR2(200),
    CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE users (
    id NUMBER PRIMARY KEY,
    username VARCHAR2(50) NOT NULL UNIQUE,
    password VARCHAR2(255) NOT NULL,
    enabled NUMBER(1) DEFAULT 1
);

CREATE TABLE user_roles (
    user_id NUMBER,
    role VARCHAR2(50) NOT NULL,
    CONSTRAINT fk_user_roles FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role)
);

CREATE TABLE audit_logs (
    id NUMBER PRIMARY KEY,
    action VARCHAR2(50) NOT NULL,
    entity_type VARCHAR2(50) NOT NULL,
    description VARCHAR2(500),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
