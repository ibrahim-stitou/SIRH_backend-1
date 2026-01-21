CREATE TABLE experience (
                             id BIGSERIAL PRIMARY KEY,

                             title VARCHAR(255) NOT NULL,
                             company VARCHAR(255) NOT NULL,
                             start_date DATE NOT NULL,
                             end_date DATE,
                             description VARCHAR(1000),

                             employee_id BIGINT NOT NULL,

                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_experience_employee FOREIGN KEY (employee_id)
                                 REFERENCES employee(id)
                                 ON DELETE CASCADE
);