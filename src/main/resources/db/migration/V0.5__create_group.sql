CREATE TABLE groups (
                            id VARCHAR(50) PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            name_ar VARCHAR(255),
                            manager_id BIGINT UNIQUE,
                            code VARCHAR(50),
                            siege_id BIGINT NOT NULL,
                            description TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,


                            CONSTRAINT fk_group_manager FOREIGN KEY (manager_id)
                                REFERENCES employees(id)
                                ON DELETE SET NULL,
                            CONSTRAINT fk_group_hq FOREIGN KEY (siege_id)
                                REFERENCES sieges(id)
                                ON DELETE CASCADE
);