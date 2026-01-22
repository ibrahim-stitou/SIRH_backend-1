CREATE TABLE skills (
                        id BIGSERIAL PRIMARY KEY,

                        skill_name VARCHAR(255) NOT NULL,
                        skill_level VARCHAR(50),

                        employee_id BIGINT NOT NULL,

                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_skill_employee FOREIGN KEY (employee_id)
                            REFERENCES employees(id)
                            ON DELETE CASCADE
);