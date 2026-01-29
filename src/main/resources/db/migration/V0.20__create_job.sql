CREATE TABLE contract_job (
                              id BIGSERIAL PRIMARY KEY ,
                              contract_id BIGINT NOT NULL,
                              metier VARCHAR(100),
                              emploi VARCHAR(100),
                              poste VARCHAR(100),
                              work_mode VARCHAR(50),
                              classification VARCHAR(50),
                              work_location VARCHAR(100),
                              level VARCHAR(50),
                              responsibilities TEXT,

                              created_by VARCHAR,
                              created_date TIMESTAMP,
                              last_modified_by VARCHAR,
                              last_modified_date TIMESTAMP ,

                              CONSTRAINT fk_contract_job_contract FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE CASCADE
);