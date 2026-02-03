-- Table métier
CREATE TABLE metier (
                        id BIGSERIAL PRIMARY KEY,
                        code VARCHAR(50) NOT NULL UNIQUE,
                        libelle VARCHAR(100) NOT NULL,
                        domaine VARCHAR(100),
                        statut VARCHAR(20) DEFAULT 'actif'
);

-- Table emploi (associe un emploi à un métier)
CREATE TABLE emploi (
                        id BIGSERIAL PRIMARY KEY,
                        metier_id BIGINT NOT NULL,
                        code VARCHAR(50) NOT NULL,
                        libelle VARCHAR(100) NOT NULL,
                        statut VARCHAR(20) DEFAULT 'actif',

                        CONSTRAINT fk_emploi_metier FOREIGN KEY (metier_id) REFERENCES metier(id) ON DELETE CASCADE,
                        CONSTRAINT uq_emploi_metier_code UNIQUE (metier_id, code)
);

-- Table poste (associe un poste à un emploi)
CREATE TABLE poste (
                       id BIGSERIAL PRIMARY KEY,
                       emploi_id BIGINT NOT NULL,
                       departement_id BIGINT NOT NULL ,
                       code VARCHAR(50) NOT NULL,
                       libelle VARCHAR(100) NOT NULL,
                       statut VARCHAR(20) DEFAULT 'actif',

                       CONSTRAINT fk_poste_emploi FOREIGN KEY (emploi_id) REFERENCES emploi(id) ON DELETE CASCADE,
                       CONSTRAINT uq_poste_emploi_code UNIQUE (emploi_id, code),
                       CONSTRAINT fk_poste_departement FOREIGN KEY (departement_id) REFERENCES departments(id)

);

-- Table contract_job (associe un contrat à un poste)
CREATE TABLE contract_job (
                              id BIGSERIAL PRIMARY KEY,
                              contract_id BIGINT NOT NULL,
                              poste_id BIGINT NOT NULL,
                              work_mode VARCHAR(50),
                              classification VARCHAR(50),
                              level VARCHAR(50),
                              responsibilities TEXT,

                              created_by VARCHAR(50),
                              created_date TIMESTAMP,
                              last_modified_by VARCHAR(50),
                              last_modified_date TIMESTAMP,

                              CONSTRAINT fk_contract_job_contract FOREIGN KEY (contract_id) REFERENCES contracts(id) ON DELETE CASCADE,
                              CONSTRAINT fk_contract_job_poste FOREIGN KEY (poste_id) REFERENCES poste(id)
);
