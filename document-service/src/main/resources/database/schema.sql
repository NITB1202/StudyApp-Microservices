CREATE TABLE user_usage (
    user_id UUID PRIMARY KEY,
    used BIGINT NOT NULL,
    total BIGINT NOT NULL
);

CREATE TABLE team_usage (
    team_id UUID PRIMARY KEY,
    used BIGINT NOT NULL,
    total BIGINT NOT NULL
);

CREATE TABLE folders (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_by UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_by UUID NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    bytes BIGINT NOT NULL,
    document_count INTEGER NOT NULL,
    team_id UUID
);

CREATE TABLE documents (
    id UUID PRIMARY KEY,
    folder_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_by UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_by UUID NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    bytes BIGINT,
    url VARCHAR(255)
);
