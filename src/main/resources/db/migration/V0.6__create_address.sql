CREATE TABLE addresses (
                           id BIGSERIAL PRIMARY KEY,

                           street VARCHAR(255),
                           city VARCHAR(255) NOT NULL,
                           postal_code VARCHAR(50),
                           country VARCHAR(255) NOT NULL,

                           employee_id BIGINT UNIQUE,
                           siege_id BIGINT UNIQUE,

                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_addresses_employee FOREIGN KEY (employee_id)
                               REFERENCES employees(id)
                               ON DELETE CASCADE,
                           CONSTRAINT fk_addresses_hq FOREIGN KEY (siege_id)
                               REFERENCES sieges(id)
                               ON DELETE CASCADE
);

CREATE INDEX idx_address_city_trgm ON addresses USING gin (lower(city) gin_trgm_ops);
CREATE INDEX idx_address_country_trgm ON addresses USING gin (lower(country) gin_trgm_ops);