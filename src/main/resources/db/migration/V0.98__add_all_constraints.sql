-- ============================
-- FK Employees
-- ============================

ALTER TABLE employees
    ADD CONSTRAINT fk_employee_group FOREIGN KEY (group_id)
        REFERENCES groups(id)
        ON DELETE RESTRICT;