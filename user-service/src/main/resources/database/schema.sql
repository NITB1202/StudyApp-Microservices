CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    dob DATE,
    gender VARCHAR(50),
    avatar_url VARCHAR(255)
);
