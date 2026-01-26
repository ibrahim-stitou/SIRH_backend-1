CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       status VARCHAR(20) DEFAULT 'ACTIVE',
                       created_by VARCHAR(255) NOT NULL,
                       created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       last_modified_by VARCHAR(255),
                       last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);