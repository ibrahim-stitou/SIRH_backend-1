CREATE TABLE company (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         name_ar VARCHAR(255),
                         phone VARCHAR(50),
                         email VARCHAR(100),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);