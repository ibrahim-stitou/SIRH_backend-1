-- ============================
-- FK Employees
-- ============================

ALTER TABLE employees
    ADD CONSTRAINT fk_employee_group FOREIGN KEY (group_id)
        REFERENCES groups(id)
        ON DELETE RESTRICT;


CREATE TABLE contract_clauses (
                                  contract_id BIGINT NOT NULL,
                                  clause_id VARCHAR(50) NOT NULL,
                                  is_active BOOLEAN DEFAULT TRUE,
                                  added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (contract_id, clause_id),
                                  CONSTRAINT fk_contract_clause_contract FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE CASCADE,
                                  CONSTRAINT fk_contract_clause_clause FOREIGN KEY (clause_id) REFERENCES clauses(id) ON DELETE CASCADE
);

CREATE TABLE trial_period_criteria (
                                       trial_period_id BIGINT NOT NULL,
                                       trial_criteria_id BIGINT NOT NULL,
                                       PRIMARY KEY (trial_period_id, trial_criteria_id),
                                       CONSTRAINT fk_trial_period_criteria_period FOREIGN KEY (trial_period_id) REFERENCES trial_periods(id) ON DELETE CASCADE,
                                       CONSTRAINT fk_trial_period_criteria_criteria FOREIGN KEY (trial_criteria_id) REFERENCES trial_criteria(id) ON DELETE CASCADE
);