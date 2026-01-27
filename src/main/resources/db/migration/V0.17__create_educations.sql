CREATE TABLE educations (
                            id BIGSERIAL PRIMARY KEY,

                            level VARCHAR(255),
                            diploma VARCHAR(255) NOT NULL,
                            year INT NOT NULL CHECK (year >= 1900),
                            institution VARCHAR(255) NOT NULL,

                            employee_id BIGINT NOT NULL,

                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_education_employee FOREIGN KEY (employee_id)
                                REFERENCES employees(id)
                                ON DELETE CASCADE
);