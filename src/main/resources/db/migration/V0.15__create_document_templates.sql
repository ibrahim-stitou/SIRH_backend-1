CREATE TABLE document_templates (
                                    id BIGSERIAL PRIMARY KEY,

                                    name VARCHAR(255) NOT NULL,

                                    purpose VARCHAR(50) NOT NULL,

                                    file_path VARCHAR(500) NOT NULL,

                                    active BOOLEAN NOT NULL DEFAULT TRUE,

                                    created_at TIMESTAMP NOT NULL,

                                    created_by VARCHAR(255) NOT NULL
);