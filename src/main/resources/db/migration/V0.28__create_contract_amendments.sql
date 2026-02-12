CREATE TABLE contract_amendments (
                                     id BIGSERIAL PRIMARY KEY,

                                     reference VARCHAR(50) UNIQUE NOT NULL,
                                     contract_id BIGINT NOT NULL,

                                     numero SERIAL NOT NULL,
                                     amendment_date DATE NOT NULL,

                                     effective_date DATE NOT NULL,

                                     objet VARCHAR(255) NOT NULL,
                                     motif TEXT,
                                     description TEXT,

                                     status VARCHAR(30) NOT NULL,
                                     type_modification VARCHAR(50) NOT NULL,

                                     notes TEXT,

                                     created_by VARCHAR,
                                     created_date TIMESTAMP NOT NULL DEFAULT now(),
                                     last_modified_by VARCHAR,
                                     last_modified_date TIMESTAMP,

                                     CONSTRAINT fk_amendment_contract
                                         FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE CASCADE

);

CREATE INDEX idx_amendment_status_type_created
    ON contract_amendments(status, type_modification, created_date DESC);

CREATE INDEX idx_amendment_contract
    ON contract_amendments(contract_id);

CREATE INDEX idx_amendment_objet_trgm
    ON contract_amendments
        USING gin (objet gin_trgm_ops);

CREATE EXTENSION IF NOT EXISTS pg_trgm;