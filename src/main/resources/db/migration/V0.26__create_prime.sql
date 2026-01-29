CREATE TABLE primes (
                                 id BIGSERIAL PRIMARY KEY,
                                 contract_salary_id BIGINT NOT NULL,
                                 prime_type_id VARCHAR(50),
                                 label VARCHAR(255) NOT NULL,
                                 amount DECIMAL(15, 2) NOT NULL,
                                 is_taxable BOOLEAN DEFAULT TRUE,
                                 is_subject_to_cnss BOOLEAN DEFAULT TRUE,

                                 CONSTRAINT fk_contract_prime_salary FOREIGN KEY (contract_salary_id)
                                     REFERENCES contract_salary(id) ON DELETE CASCADE
);
