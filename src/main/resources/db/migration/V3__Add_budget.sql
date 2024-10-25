CREATE TABLE budget
(
    id       UUID           NOT NULL,
    "limit"  DECIMAL(10, 2) NOT NULL,
    category VARCHAR(50)    NOT NULL,
    user_id  UUID           NOT NULL,
    CONSTRAINT pk_budget PRIMARY KEY (id)
);

ALTER TABLE budget
    ADD CONSTRAINT FK_BUDGET_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);