CREATE TABLE departments (
                             id BIGSERIAL PRIMARY KEY,

                             name VARCHAR(255) NOT NULL UNIQUE,
                             name_ar VARCHAR(255),

                             description TEXT,

                             status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

                             company_id BIGINT NOT NULL,

                             created_by VARCHAR(255) NOT NULL,
                             created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             last_modified_by VARCHAR(255),
                             last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_department_company FOREIGN KEY (company_id)
                             REFERENCES company(id)
                             ON DELETE CASCADE
);