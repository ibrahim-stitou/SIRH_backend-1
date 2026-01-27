CREATE TABLE sieges (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        code VARCHAR(50) UNIQUE NOT NULL,
                        company_id BIGINT NOT NULL,
                        address_id BIGINT,
                        phone VARCHAR(50),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                        CONSTRAINT fk_hq_company FOREIGN KEY (company_id)
                            REFERENCES company(id)
                            ON DELETE CASCADE
);