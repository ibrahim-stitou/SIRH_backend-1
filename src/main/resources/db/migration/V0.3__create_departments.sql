CREATE TABLE departments (
                             id BIGSERIAL PRIMARY KEY,

                             name VARCHAR(255) NOT NULL UNIQUE,
                             name_ar VARCHAR(255),

                             description TEXT,

                             company_id BIGINT NOT NULL,

                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_department_company FOREIGN KEY (company_id)
                             REFERENCES company(id)
                             ON DELETE CASCADE
);