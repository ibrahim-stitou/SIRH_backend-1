CREATE TABLE addresses (
                           id BIGSERIAL PRIMARY KEY,

                           street VARCHAR(255),
                           city VARCHAR(255) NOT NULL,
                           postal_code VARCHAR(50),
                           country VARCHAR(255) NOT NULL,

                           employee_id BIGINT NOT NULL UNIQUE,

                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_addresses_employee FOREIGN KEY (employee_id)
                               REFERENCES employees(id)
                               ON DELETE CASCADE
);