CREATE TABLE contracts (
                          id BIGSERIAL PRIMARY KEY,
                          reference VARCHAR(100) UNIQUE NOT NULL,
                          type VARCHAR(50) NOT NULL,
                          status VARCHAR(50) NOT NULL,
                          employee_id BIGINT NOT NULL,
                          description TEXT,
                          signature_date DATE NOT NULL,
                          start_date DATE NOT NULL,
                          effective_start_date DATE NULL,
                          end_date DATE NULL,

                          created_by VARCHAR,
                          created_date TIMESTAMP,
                          last_modified_by VARCHAR,
                          last_modified_date TIMESTAMP ,

                          CONSTRAINT fk_contract_employee FOREIGN KEY (employee_id) REFERENCES employees(id)
);

CREATE UNIQUE INDEX unique_active_contract_per_employee
    ON contracts(employee_id)
    WHERE status = 'ACTIF';