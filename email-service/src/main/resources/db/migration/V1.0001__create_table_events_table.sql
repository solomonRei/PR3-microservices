CREATE TABLE IF NOT EXISTS "events"
(
    id           bigserial   NOT NULL PRIMARY KEY,
    type         VARCHAR(30) NOT NULL,
    data         JSONB       NOT NULL,
    is_processed BOOLEAN DEFAULT FALSE
)