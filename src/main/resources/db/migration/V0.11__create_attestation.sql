CREATE TABLE attestations (
                             id BIGSERIAL PRIMARY KEY,
                             request_id BIGINT UNIQUE NOT NULL,
                             type_attestation VARCHAR(50) NOT NULL,
                             numero_attestation VARCHAR(100) UNIQUE NOT NULL,
                             date_generation DATE,
                             notes TEXT,
                             created_at TIMESTAMP,
                             updated_at TIMESTAMP,
                             CONSTRAINT fk_request FOREIGN KEY (request_id) REFERENCES demande_attestation(id)
                         );

CREATE INDEX idx_attestation_type ON attestations(type_attestation);
