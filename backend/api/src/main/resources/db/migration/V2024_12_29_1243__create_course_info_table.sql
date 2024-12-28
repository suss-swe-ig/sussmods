CREATE TABLE course_info
(
    id                   SERIAL PRIMARY KEY,
    code                 VARCHAR(10) UNIQUE NOT NULL,
    name                 VARCHAR(50)        NOT NULL,
    level                VARCHAR(10),
    credit_units         VARCHAR(20),
    language             VARCHAR(20),
    presentation_pattern VARCHAR(50),
    synopsis             TEXT,
    topics               JSONB,
    textbooks            JSONB,
    learning_outcome     JSONB
);
