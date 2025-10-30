DROP TABLE IF EXISTS visit CASCADE;
DROP TABLE IF EXISTS school_year CASCADE;
DROP TABLE IF EXISTS mentor CASCADE;
DROP TABLE IF EXISTS student CASCADE;
DROP TABLE IF EXISTS company CASCADE;
DROP TABLE IF EXISTS app_user CASCADE;

CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE company (
    id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(200),
    access_information VARCHAR(500)
);

CREATE TABLE student (
    id BIGSERIAL PRIMARY KEY,
    last_name VARCHAR(50) NOT NULL,
    first_name VARCHAR(50),
    email VARCHAR(50) NOT NULL UNIQUE,
    phone VARCHAR(10),
    is_archived BOOLEAN
);

CREATE TABLE mentor (
    id BIGSERIAL PRIMARY KEY,
    last_name VARCHAR(50) NOT NULL,
    first_name VARCHAR(50),
    job_title VARCHAR(100),
    email VARCHAR(50) UNIQUE,
    phone VARCHAR(10),
    comment VARCHAR(500),
    company_id BIGINT NOT NULL,
    CONSTRAINT fk_mentor_company FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE CASCADE
);

CREATE TABLE school_year (
    id BIGSERIAL PRIMARY KEY,
    program VARCHAR(100),
    academic_year VARCHAR(10) NOT NULL,
    major VARCHAR(100),
    comment VARCHAR(500),
    feedback VARCHAR(500),
    
    keywords VARCHAR(100),
    target_job VARCHAR(100),
    mission_comment VARCHAR(500),
    
    subject VARCHAR(100),
    report_grade DECIMAL(5,2),
    report_comment VARCHAR(500),
    
    date TIMESTAMP,
    presentation_grade DECIMAL(5,2),
    presentation_comment VARCHAR(500),
    
    mentor_id BIGINT,
    company_id BIGINT,
    student_id BIGINT NOT NULL,
    
    CONSTRAINT fk_schoolyear_mentor FOREIGN KEY (mentor_id) REFERENCES mentor(id) ON DELETE SET NULL,
    CONSTRAINT fk_schoolyear_company FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE SET NULL,
    CONSTRAINT fk_schoolyear_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
    
    CONSTRAINT chk_program CHECK (program IN ('L1_APP', 'L2_APP', 'L3_APP', 'M1_APP', 'M2_APP'))
);

CREATE TABLE visit (
    id BIGSERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    format VARCHAR(20) NOT NULL,
    comment VARCHAR(500),
    school_year_id BIGINT NOT NULL,
    
    CONSTRAINT fk_visit_schoolyear FOREIGN KEY (school_year_id) REFERENCES school_year(id) ON DELETE CASCADE,
    
    CONSTRAINT chk_format CHECK (format IN ('ONLINE', 'IN_PERSON'))
);
