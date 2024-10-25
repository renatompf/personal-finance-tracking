CREATE TABLE transaction
(
    id          UUID                        NOT NULL,
    amount      DECIMAL(10, 2)              NOT NULL,
    category    VARCHAR(50)                 NOT NULL,
    description TEXT,
    date        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id     UUID                        NOT NULL,
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);