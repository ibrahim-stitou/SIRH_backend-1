CREATE TABLE department (
                             id BIGSERIAL PRIMARY KEY,

                             name VARCHAR(255) NOT NULL UNIQUE,
                             name_ar VARCHAR(255),

                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
