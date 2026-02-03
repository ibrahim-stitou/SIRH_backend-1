CREATE TABLE app_parameters (
                                id BIGINT PRIMARY KEY,
                                type VARCHAR(50) UNIQUE NOT NULL,
                                max_value DECIMAL(15,2) NOT NULL,
                                description TEXT,
                                is_required BOOLEAN DEFAULT TRUE
);