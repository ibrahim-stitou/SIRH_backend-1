CREATE TABLE sieges (
                        id BIGSERIAL PRIMARY KEY,
                        nom VARCHAR(255) NOT NULL,
                        manager_id BIGINT REFERENCES employees(id)
);