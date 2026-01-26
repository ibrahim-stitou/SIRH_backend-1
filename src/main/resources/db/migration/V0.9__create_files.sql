CREATE TABLE files (
                      id BIGSERIAL PRIMARY KEY,
                      entity_type VARCHAR(100) NOT NULL,
                      entity_id BIGINT NOT NULL,
                      purpose varchar(100) NOT NULL,
                      title VARCHAR(255) NOT NULL,
                      file_name VARCHAR(255) NOT NULL,
                      mime_type VARCHAR(100) NOT NULL,
                      storage_path VARCHAR(255) NOT NULL ,
                      uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      uploaded_by VARCHAR(100) NOT NULL,
                      description TEXT,
                      deleted_at TIMESTAMP,
                      created_by VARCHAR(255) NOT NULL,
                      created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      last_modified_by VARCHAR(255),
                      last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_files_entity
    ON files(entity_type, entity_id);

CREATE INDEX idx_files_entity_type
    ON files(entity_type);

CREATE INDEX idx_files_entity_purpose
    ON files(entity_type, entity_id, purpose);

CREATE UNIQUE INDEX uq_files_active
    ON files(entity_type, entity_id, purpose)
    WHERE deleted_at IS NULL;
