CREATE TABLE transaction (
                             id UUID NOT NULL,
                             date TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                             user_id UUID,
                             amount NUMERIC(10, 2),
                             category VARCHAR(50),
                             description TEXT,
                             PRIMARY KEY (id, date),
                             CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);