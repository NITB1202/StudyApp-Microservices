CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    provider VARCHAR(255) NOT NULL,
    provider_id VARCHAR(255),
    email VARCHAR(255),
    hashed_password VARCHAR(255),
    role VARCHAR(255) NOT NULL
);
