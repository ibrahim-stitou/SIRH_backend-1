CREATE TABLE certification (
                                id BIGSERIAL PRIMARY KEY,

                                name VARCHAR(255) NOT NULL,
                                issuer VARCHAR(255),
                                issue_date DATE,

                                employee_id BIGINT NOT NULL,

                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_certification_employee FOREIGN KEY (employee_id)
                                    REFERENCES employee(id)
                                    ON DELETE CASCADE
);