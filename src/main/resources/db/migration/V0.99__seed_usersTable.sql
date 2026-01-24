-- ========================================
-- Seeder for "users" table for testing
-- ========================================

-- Clear existing data
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- PASSWORD : password123

-- Insert ADMIN user
INSERT INTO users (email, password_hash, role, status, created_at)
VALUES (
           'admin@example.com',
           '$2a$10$7Qf7hz8Vnqz1H1lE9E0Uve0Z1R6zR1Sb/OTX7UMx0VwKnV27FSh0i',
           'ADMIN',
           'ACTIVE',
           NOW()
       );

-- Insert HR_MANAGER user
INSERT INTO users (email, password_hash, role, status, created_at)
VALUES (
           'hrmanager@example.com',
           '$2a$10$7Qf7hz8Vnqz1H1lE9E0Uve0Z1R6zR1Sb/OTX7UMx0VwKnV27FSh0i',
           'HR_MANAGER',
           'ACTIVE',
           NOW()
       );

-- Insert EMPLOYEE user
INSERT INTO users (email, password_hash, role, status, created_at)
VALUES (
           'employee@example.com',
           '$2a$10$7Qf7hz8Vnqz1H1lE9E0Uve0Z1R6zR1Sb/OTX7UMx0VwKnV27FSh0i',
           'EMPLOYEE',
           'ACTIVE',
           NOW()
       );

-- Insert INACTIVE user
INSERT INTO users (email, password_hash, role, status, created_at)
VALUES (
           'inactive@example.com',
           '$2a$10$7Qf7hz8Vnqz1H1lE9E0Uve0Z1R6zR1Sb/OTX7UMx0VwKnV27FSh0i',
           'EMPLOYEE',
           'INACTIVE',
           NOW()
       );

-- Insert SUSPENDED user
INSERT INTO users (email, password_hash, role, status, created_at)
VALUES (
           'suspended@example.com',
           '$2a$10$7Qf7hz8Vnqz1H1lE9E0Uve0Z1R6zR1Sb/OTX7UMx0VwKnV27FSh0i',
           'EMPLOYEE',
           'SUSPENDED',
           NOW()
       );