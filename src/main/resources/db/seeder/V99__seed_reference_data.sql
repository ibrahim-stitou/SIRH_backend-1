-- =========================================
-- Seed Company
-- =========================================
INSERT INTO company (id,name, name_ar, phone, email, created_at, updated_at)
VALUES
    (1,'TechCorp', 'تك كورب', '+212 530000000', 'contact@techcorp.com', NOW(), NOW());


-- =========================================
-- Seed Sieges (Headquarters)
-- =========================================
INSERT INTO sieges (name, code, company_id, phone, created_at, updated_at)
VALUES
    ('Siège Central', 'HQ-CENTRAL', 1, '+212530000001', NOW(), NOW()),
    ('Siège Sud', 'HQ-SUD', 1, '+212530000002', NOW(), NOW());


-- =========================================
-- Seed Departments
-- =========================================
INSERT INTO departments (id, name, name_ar, description, status, company_id, created_by, created_date, last_modified_by, last_modified_date)
VALUES
    (1, 'IT', 'تقنية المعلومات', 'Département IT', 'ACTIVE', 1, 'system', NOW(), 'system', NOW()),
    (2, 'RH', 'الموارد البشرية', 'Département RH', 'ACTIVE', 1, 'system', NOW(), 'system', NOW()),
    (3, 'Finance', 'المالية', 'Département Finance', 'ACTIVE', 1, 'system', NOW(), 'system', NOW()),
    (4, 'Marketing', 'التسويق', 'Département Marketing', 'ACTIVE', 1, 'system', NOW(), 'system', NOW());

-- =========================================
-- Seed Groups (with manager_id pointing to employee)
-- =========================================
INSERT INTO groups (id, name, code, siege_id, description, manager_id)
VALUES
    (1,'Groupe Finance','FIN',1,'Groupe Finance', NULL),
    (2,'Groupe RH','HR',1,'Groupe RH', NULL);

-- =========================================
-- Seed Employees
-- =========================================
INSERT INTO employees (id, matricule, first_name, last_name, first_name_ar, last_name_ar, status, cin, birth_date, birth_place, gender, nationality, marital_status, number_of_children, phone, email, aptitude_medical, bank_name, rib, department_id, group_id, created_by)
VALUES
    (1,'EMP001','Ahmed','El Amrani','أحمد','الأمراني','ACTIF','CIN001','1985-03-15','Casablanca','Male','Marocaine','MARIE',2,'0600000001','ahmed.elamrani@example.com','APTE','Attijariwafa Bank','RIB001',1,1,'SYSTEM'),
    (2,'EMP002','Fatima','El Fassi','فاطمة','الفاسي','ACTIF','CIN002','1990-06-22','Rabat','Female','Marocaine','CELIBATAIRE',0,'0600000002','fatima.elfassi@example.com','APTE','BMCE Bank','RIB002',2,2,'SYSTEM'),
    (3,'EMP003','Mohamed','Benjelloun','محمد','بنجلون','SUSPENDU','CIN003','1982-11-05','Marrakech','Male','Marocaine','MARIE',3,'0600000003','mohamed.benjelloun@example.com','APTE','CIH Bank','RIB003',3,1,'SYSTEM'),
    (4,'EMP004','Salma','Bennani','سلمى','بناني','ACTIF','CIN004','1995-01-18','Fès','Female','Marocaine','CELIBATAIRE',0,'0600000004','salma.bennani@example.com','APTE','Attijariwafa Bank','RIB004',3,2,'SYSTEM'),
    (5,'EMP005','Youssef','El Idrissi','يوسف','الإدريسي','PARTI','CIN005','1988-08-30','Tanger','Male','Marocaine','DIVORCE',1,'0600000005','youssef.elidrissi@example.com','APTE','BMCE Bank','RIB005',4,1,'SYSTEM'),
    (6,'EMP006','Khadija','El Khattabi','خديجة','الخطابي','ACTIF','CIN006','1992-12-12','Casablanca','Female','Marocaine','MARIE',2,'0600000006','khadija.elkhattabi@example.com','APTE','CIH Bank','RIB006',2,2,'SYSTEM'),
    (7,'EMP007','Omar','El Fadli','عمر','الفاضلي','ACTIF','CIN007','1987-05-20','Rabat','Male','Marocaine','MARIE',3,'0600000007','omar.elfadli@example.com','APTE','Attijariwafa Bank','RIB007',1,1,'SYSTEM'),
    (8,'EMP008','Imane','El Youssoufi','إيمان','اليوسفي','ACTIF','CIN008','1993-07-07','Marrakech','Female','Marocaine','CELIBATAIRE',0,'0600000008','imane.elyoussoufi@example.com','APTE','BMCE Bank','RIB008',2,2,'SYSTEM'),
    (9,'EMP009','Hassan','El Ouazzani','حسن','الوزاني','SUSPENDU','CIN009','1984-09-11','Fès','Male','Marocaine','MARIE',4,'0600000009','hassan.elouazzani@example.com','APTE','CIH Bank','RIB009',3,1,'SYSTEM'),
    (10,'EMP010','Sara','El Malki','سارة','المكي','ACTIF','CIN010','1991-04-25','Tanger','Female','Marocaine','CELIBATAIRE',0,'0600000010','sara.elmalki@example.com','APTE','Attijariwafa Bank','RIB010',4,2,'SYSTEM');


UPDATE groups SET manager_id = 1 WHERE id = 1;
UPDATE groups SET manager_id = 2 WHERE id = 2;

-- =========================================
-- Seed Addresses for Employees
-- =========================================
INSERT INTO addresses (street, city, postal_code, country, employee_id)
VALUES
    ('123 Avenue Hassan II','Casablanca','20000','Maroc',1),
    ('45 Rue Oued Fes','Rabat','10000','Maroc',2),
    ('12 Boulevard Mohamed V','Marrakech','40000','Maroc',3);

-- =========================================
-- Seed Addresses for Sieges
-- =========================================
INSERT INTO addresses (street, city, postal_code, country, siege_id)
VALUES
    ('123 Avenue Mohamed V','Rabat','10000','Maroc',1),
    ('77 Boulevard Zerktouni','Casablanca','20000','Maroc',2);

-- =========================================
-- Seed Educations
-- =========================================
INSERT INTO educations (id, level, diploma, year, institution, employee_id)
VALUES
    (1, 'Licence', 'Informatique', 2007, 'Université Hassan II', 1),
    (2, 'Master', 'Gestion des Ressources Humaines', 2012, 'Université Mohammed V', 2),
    (3, 'Licence', 'Finance', 2004, 'Université Cadi Ayyad', 3),
    (4, 'Master', 'Marketing', 2017, 'Université Sidi Mohamed Ben Abdellah', 4),
    (5, 'Licence', 'Génie industriel', 2010, 'ENSAM Marrakech', 5),
    (6, 'Licence', 'Ressources Humaines', 2014, 'Université Hassan II', 6),
    (7, 'Master', 'Informatique', 2011, 'Université Mohammed V', 7),
    (8, 'Licence', 'Marketing', 2016, 'Université Al Akhawayn', 8),
    (9, 'Licence', 'Finance', 2003, 'Université Sidi Mohamed Ben Abdellah', 9),
    (10, 'Master', 'Génie industriel', 2015, 'ENSAM Casablanca', 10);

-- =========================================
-- Seed Certifications
-- =========================================
INSERT INTO certifications (id, name, issuer, issue_date, expiration_date, employee_id)
VALUES
    (1, 'PMP', 'PMI', '2015-06-01', '2025-06-01', 1),
    (2, 'HR Professional', 'AMHR', '2018-09-15', '2023-09-15', 2),
    (3, 'CFA Level 1', 'CFA Institute', '2006-05-20', NULL, 3),
    (4, 'Digital Marketing', 'Google', '2018-11-10', NULL, 4),
    (5, 'Lean Six Sigma Green Belt', 'IASSC', '2011-03-05', NULL, 5),
    (6, 'HR Analytics', 'Coursera', '2016-07-12', NULL, 6),
    (7, 'Java Certification', 'Oracle', '2012-08-01', NULL, 7),
    (8, 'Marketing Analytics', 'Google', '2017-10-20', NULL, 8),
    (9, 'Financial Modeling', 'CFI', '2005-04-15', NULL, 9),
    (10, 'Project Management', 'PMI', '2016-01-25', NULL, 10);

-- =========================================
-- Seed Skills
-- =========================================
INSERT INTO skills (id, skill_name, skill_level, employee_id)
VALUES
    (1, 'Java', 5, 1),
    (2, 'SQL', 4, 1),
    (3, 'Communication', 5, 2),
    (4, 'Recruitment', 4, 2),
    (5, 'Financial Analysis', 5, 3),
    (6, 'Excel', 4, 3),
    (7, 'SEO', 4, 4),
    (8, 'Social Media Marketing', 5, 4),
    (9, 'Lean Management', 5, 5),
    (10, 'Process Optimization', 4, 5),
    (11, 'Employee Relations', 4, 6),
    (12, 'Payroll Management', 5, 6),
    (13, 'Spring Boot', 5, 7),
    (14, 'REST APIs', 4, 7),
    (15, 'Market Research', 4, 8),
    (16, 'Content Creation', 5, 8),
    (17, 'Budgeting', 5, 9),
    (18, 'Forecasting', 4, 9),
    (19, 'Project Planning', 4, 10),
    (20, 'Quality Control', 5, 10);

-- =========================================
-- Seed Experiences
-- =========================================
INSERT INTO experiences (id, title, company, start_date, end_date, description, employee_id)
VALUES
    (1, 'Développeur Junior', 'Inwi', '2008-01-01', '2011-12-31', 'Développement de modules internes.', 1),
    (2, 'HR Specialist', 'Royal Air Maroc', '2013-01-01', '2017-06-30', 'Gestion des recrutements et formations.', 2),
    (3, 'Analyste Financier', 'BMCE Bank', '2005-01-01', '2010-12-31', 'Analyse financière et reporting.', 3),
    (4, 'Digital Marketer', 'Maroc Telecom', '2017-01-01', NULL, 'Campagnes marketing en ligne.', 4),
    (5, 'Ingénieur Production', 'Renault Maroc', '2010-02-01', '2016-12-31', 'Optimisation des lignes de production.', 5),
    (6, 'HR Coordinator', 'OCP', '2015-01-01', NULL, 'Coordination du personnel et paie.', 6),
    (7, 'Backend Developer', 'Capgemini Maroc', '2012-01-01', NULL, 'Développement de services backend.', 7),
    (8, 'Marketing Analyst', 'Marjane', '2016-03-01', NULL, 'Analyse des campagnes marketing.', 8),
    (9, 'Financial Consultant', 'Attijariwafa Bank', '2004-05-01', '2012-12-31', 'Conseil financier aux clients.', 9),
    (10, 'Project Manager', 'Bombardier Maroc', '2015-06-01', NULL, 'Gestion de projets industriels.', 10);

-- =========================================
-- Seed Person In Charge (1 per employee)
-- =========================================
INSERT INTO person_in_charge (id, name, phone, relationship, cin, birth_date, employee_id)
VALUES
    (1, 'Mohamed El Amrani', '0610000001', 'PERE', 'CINPC001', '1960-05-10', 1),
    (2, 'Latifa El Fassi', '0610000002', 'MERE', 'CINPC002', '1965-08-20', 2),
    (3, 'Ahmed Benjelloun', '0610000003', 'PERE', 'CINPC003', '1958-11-30', 3),
    (4, 'Samira Bennani', '0610000004', 'MERE', 'CINPC004', '1968-02-15', 4),
    (5, 'Khalid El Idrissi', '0610000005', 'CONJOINT', 'CINPC005', '1985-09-05', 5),
    (6, 'Hassan El Khattabi', '0610000006', 'PERE', 'CINPC006', '1962-12-12', 6),
    (7, 'Fatima El Fadli', '0610000007', 'MERE', 'CINPC007', '1964-03-20', 7),
    (8, 'Rachid El Youssoufi', '0610000008', 'PERE', 'CINPC008', '1960-07-07', 8),
    (9, 'Saadia El Ouazzani', '0610000009', 'MERE', 'CINPC009', '1959-09-11', 9),
    (10, 'Mohammed El Malki', '0610000010', 'CONJOINT', 'CINPC010', '1987-04-25', 10);


-- Seed DocumentTemplate table

INSERT INTO document_templates (name, purpose, file_path, active, created_at, created_by)
VALUES
    -- Attestation / Salaire
    ('default', 'SALAIRE', '/templates/Attestation/Salaire/default.html', TRUE, NOW(), 'system'),

    -- Attestation / Stage
    ('default', 'STAGE', '/templates/Attestation/Stage/default.html', TRUE, NOW(), 'system'),

    -- Attestation / Travail
    ('default', 'TRAVAIL', '/templates/Attestation/Travail/default.html', TRUE, NOW(), 'system'),

    -- Bulletin de paie
    ('default', 'SALAIRE', '/templates/Bulletin de paie/default.html', TRUE, NOW(), 'system'),

    -- Contrat
    ('default', 'TRAVAIL', '/templates/Contrat/default.html', TRUE, NOW(), 'system'),
    ('default', 'GENERATED', '/templates/Contrat/default.html', TRUE, NOW(), 'system');


-- Seed demande_attestation

INSERT INTO demande_attestation
(employee_id, type_attestation, date_request, date_souhaitee, status, note, date_validation, created_at, updated_at)
VALUES
-- Exemple 1
(1, 'SALARY', CURRENT_DATE, CURRENT_DATE + INTERVAL '7 days', 'PENDING', 'Demande de salaire', NULL, NOW(), NOW()),

-- Exemple 2
(2, 'EMPLOYMENT', CURRENT_DATE, CURRENT_DATE + INTERVAL '10 days', 'PENDING', 'Demande emploi', NULL, NOW(), NOW()),

-- Exemple 3
(3, 'EXPERIENCE', CURRENT_DATE, CURRENT_DATE + INTERVAL '5 days', 'PENDING', 'Demande expérience', NULL, NOW(), NOW()),

-- Exemple 4
(4, 'INTERNSHIP', CURRENT_DATE, CURRENT_DATE + INTERVAL '14 days', 'PENDING', 'Demande stage', NULL, NOW(), NOW()),

-- Exemple 5
(5, 'OTHER', CURRENT_DATE, CURRENT_DATE + INTERVAL '3 days', 'PENDING', 'Autre demande', NULL, NOW(), NOW()),

-- Exemple 6
(6, 'SALARY', CURRENT_DATE, CURRENT_DATE + INTERVAL '7 days', 'PENDING', 'Demande salaire', NULL, NOW(), NOW()),

-- Exemple 7
(7, 'EMPLOYMENT', CURRENT_DATE, CURRENT_DATE + INTERVAL '10 days', 'PENDING', 'Demande emploi', NULL, NOW(), NOW()),

-- Exemple 8
(8, 'EXPERIENCE', CURRENT_DATE, CURRENT_DATE + INTERVAL '5 days', 'PENDING', 'Demande expérience', NULL, NOW(), NOW()),

-- Exemple 9
(9, 'INTERNSHIP', CURRENT_DATE, CURRENT_DATE + INTERVAL '14 days', 'PENDING', 'Demande stage', NULL, NOW(), NOW()),

-- Exemple 10
(10, 'OTHER', CURRENT_DATE, CURRENT_DATE + INTERVAL '3 days', 'PENDING', 'Autre demande', NULL, NOW(), NOW());


INSERT INTO metier (id, code, libelle, domaine, statut) VALUES
                                                            (1, 'DEV', 'Développement logiciel', 'IT', 'actif'),
                                                            (2, 'RH', 'Ressources humaines', 'RH', 'actif'),
                                                            (3, 'FIN', 'Finance & Comptabilité', 'Finance', 'actif'),
                                                            (4, 'OPS', 'Opérations', 'Logistique', 'actif');

INSERT INTO emploi (id, metier_id, code, libelle, statut) VALUES
-- IT
(1, 1, 'BE_DEV', 'Backend Developer', 'actif'),
(2, 1, 'FE_DEV', 'Frontend Developer', 'actif'),

-- RH
(3, 2, 'HR_GEN', 'RH Généraliste', 'actif'),

-- Finance
(4, 3, 'ACC', 'Comptable', 'actif'),

-- Operations
(5, 4, 'LOG_SUP', 'Superviseur Logistique', 'actif');


INSERT INTO poste (id, emploi_id, departement_id, code, libelle, statut) VALUES
-- IT Department (dept 1)
(1, 1, 1, 'BE_JUN', 'Backend Developer Junior', 'actif'),
(2, 1, 1, 'BE_SEN', 'Backend Developer Senior', 'actif'),
(3, 2, 1, 'FE_JUN', 'Frontend Developer Junior', 'actif'),

-- RH Department (dept 2)
(4, 3, 2, 'RH_GEN', 'RH Généraliste', 'actif'),

-- Finance Department (dept 3)
(5, 4, 3, 'ACC_STD', 'Comptable', 'actif'),

-- Operations Department (dept 4)
(6, 5, 4, 'LOG_SUP', 'Superviseur Logistique', 'actif');
