CREATE TABLE demande_attestation (
                                     id BIGSERIAL PRIMARY KEY,
                                     employee_id BIGINT NOT NULL,
                                     type_attestation VARCHAR(50) NOT NULL,
                                     date_request DATE NOT NULL,
                                     date_souhaitee DATE NOT NULL,
                                     status VARCHAR(50) NOT NULL,
                                     note TEXT,
                                     date_validation TIMESTAMP,
                                     created_at TIMESTAMP,
                                     updated_at TIMESTAMP,
                                     CONSTRAINT fk_employee FOREIGN KEY (employee_id) REFERENCES employees(id)
);

CREATE INDEX idx_demande_attestation_employee ON demande_attestation(employee_id);
CREATE INDEX idx_demande_attestation_status ON demande_attestation(status);
