CREATE TABLE contract_salary (
                                 id BIGSERIAL PRIMARY KEY ,
                                 contract_id BIGINT NOT NULL,
                                 base_salary INT NOT NULL,
                                 salary_brut DECIMAL(15, 2) NOT NULL,
                                 salary_net DECIMAL(15, 2) NOT NULL,
                                 currency VARCHAR(10) NOT NULL,
                                 payment_method VARCHAR(50),
                                 periodicity VARCHAR(50),
                                 payment_day INT CHECK (payment_day BETWEEN 1 AND 31),

                                 amendment_id BIGINT,
                                 effective_date DATE,
                                 active BOOLEAN DEFAULT TRUE,

                                 created_by VARCHAR,
                                 created_date TIMESTAMP,
                                 last_modified_by VARCHAR,
                                 last_modified_date TIMESTAMP ,


                                 CONSTRAINT fk_contract_salary_contract FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE CASCADE
);