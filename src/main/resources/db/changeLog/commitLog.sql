CREATE TABLE commit_log
(
    id                  BIGSERIAL PRIMARY KEY NOT NULL,
    operation           INT,
    json                VARCHAR(500),
    entity_name          VARCHAR(50),
    entity_id           BIGINT,
    date_time_operation TIMESTAMP
);
CREATE SEQUENCE commit_log_id
    INCREMENT 1
    START 1
    MINVALUE 1
    OWNED BY commit_log.id;