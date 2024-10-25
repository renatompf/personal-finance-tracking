CREATE TABLE token
(
    id      UUID        NOT NULL,
    token   TEXT        NOT NULL,
    type    VARCHAR(10) NOT NULL,
    expired BOOLEAN     NOT NULL,
    revoked BOOLEAN     NOT NULL,
    user_id UUID,
    CONSTRAINT pk_token PRIMARY KEY (id)
);

ALTER TABLE token
    ADD CONSTRAINT FK_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);