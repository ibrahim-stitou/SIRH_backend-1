CREATE TABLE contract_schedules (
                                    id BIGSERIAL PRIMARY KEY,
                                    contract_id BIGINT NOT NULL,
                                    schedule_type VARCHAR(50),
                                    shift_work BOOLEAN DEFAULT FALSE,
                                    hours_per_day INT,
                                    days_per_week INT,
                                    hours_per_week INT,
                                    start_time TIME,
                                    end_time TIME,
                                    break_duration INT,
                                    annual_leave_days INT,
                                    other_leaves TEXT,

                                    created_by VARCHAR,
                                    created_date TIMESTAMP,
                                    last_modified_by VARCHAR,
                                    last_modified_date TIMESTAMP ,

                                    CONSTRAINT fk_contract_schedule_contract FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE CASCADE
);