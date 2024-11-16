CREATE TABLE settings
(
    id                   UUID NOT NULL,
    monthly_budget_value NUMERIC(10, 2),
    user_id              UUID,
    CONSTRAINT pk_settings_user PRIMARY KEY (id)
);

ALTER TABLE settings
    ADD CONSTRAINT FK_SETTINGS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE transaction
    ALTER COLUMN amount SET NOT NULL;

ALTER TABLE transaction
    ALTER COLUMN category SET NOT NULL;

ALTER TABLE transaction
    ALTER COLUMN user_id SET NOT NULL;