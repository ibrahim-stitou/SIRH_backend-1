CREATE TABLE employees (
                           id BIGSERIAL PRIMARY KEY,

                           matricule VARCHAR(255) NOT NULL UNIQUE,
                           first_name VARCHAR(255) NOT NULL,
                           last_name VARCHAR(255) NOT NULL,
                           first_name_ar VARCHAR(255),
                           last_name_ar VARCHAR(255),
                           status VARCHAR(20) DEFAULT 'ACTIF',
                           cin VARCHAR(255) NOT NULL UNIQUE,
                           birth_date DATE,
                           birth_place VARCHAR(255),
                           gender VARCHAR(50),
                           nationality VARCHAR(255),
                           marital_status VARCHAR(50),
                           number_of_children INT,
                           phone VARCHAR(50),
                           email VARCHAR(255) NOT NULL UNIQUE,
                           aptitude_medical VARCHAR(255),
                           bank_name VARCHAR(255),
                           rib VARCHAR(255) UNIQUE,

                           department_id BIGINT NOT NULL,

                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_employee_department FOREIGN KEY (department_id)
                           REFERENCES departments(id)
                           ON DELETE RESTRICT
);


CREATE INDEX idx_employee_status ON employees(status);

CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_employee_name_trgm ON employees USING gin (
                                                            first_name gin_trgm_ops,
                                                            last_name gin_trgm_ops,
                                                            matricule gin_trgm_ops
    );