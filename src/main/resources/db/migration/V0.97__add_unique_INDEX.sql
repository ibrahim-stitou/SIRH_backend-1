CREATE UNIQUE INDEX uq_active_salary
    ON contract_salary(contract_id)
    WHERE active = true;

CREATE UNIQUE INDEX uq_active_job
    ON contract_job(contract_id)
    WHERE active = true;

CREATE UNIQUE INDEX uq_active_schedule
    ON contract_schedules(contract_id)
    WHERE active = true;