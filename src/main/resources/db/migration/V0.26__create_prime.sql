CREATE TABLE primes (
                                 id BIGSERIAL PRIMARY KEY,
                                 contract_salary_id BIGINT NOT NULL,
                                 prime_type_id VARCHAR(50),
                                 label VARCHAR(255) NOT NULL,
                                 amount DECIMAL(15, 2) NOT NULL,
                                 is_taxable BOOLEAN DEFAULT TRUE,
                                 is_subject_to_cnss BOOLEAN DEFAULT TRUE,

                                 created_by VARCHAR(255),
                                 created_date TIMESTAMP,
                                 last_modified_by VARCHAR(255),
                                 last_modified_date TIMESTAMP,

                                 CONSTRAINT fk_contract_prime_salary FOREIGN KEY (contract_salary_id)
                                     REFERENCES contract_salary(id) ON DELETE CASCADE
);
