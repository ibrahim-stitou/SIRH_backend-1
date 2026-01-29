CREATE TABLE trial_periods (
                               id BIGSERIAL PRIMARY KEY,
                               contract_id BIGINT NOT NULL,
                               enabled BOOLEAN DEFAULT TRUE,
                               duration_months INT DEFAULT 0,
                               duration_days INT DEFAULT 0,
                               start_date DATE NOT NULL,
                               end_date DATE NULL,
                               renewable BOOLEAN DEFAULT FALSE,
                               max_renewals INT DEFAULT 0,
                               conditions TEXT,

                               created_by VARCHAR,
                               created_date TIMESTAMP,
                               last_modified_by VARCHAR,
                               last_modified_date TIMESTAMP ,

                               CONSTRAINT fk_trial_period_contract FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE CASCADE
);