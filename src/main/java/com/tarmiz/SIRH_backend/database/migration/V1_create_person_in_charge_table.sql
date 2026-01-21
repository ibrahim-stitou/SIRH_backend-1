CREATE TABLE person_in_charge (
                                   id BIGSERIAL PRIMARY KEY,

                                   name VARCHAR(255),
                                   phone VARCHAR(50),
                                   relationship VARCHAR(100),
                                   cin VARCHAR(50),
                                   birth_date DATE,

                                   employee_id BIGINT NOT NULL,

                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                   CONSTRAINT fk_person_in_charge_employee FOREIGN KEY (employee_id)
                                       REFERENCES employee(id)
                                       ON DELETE CASCADE
);