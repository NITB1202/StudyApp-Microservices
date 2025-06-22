CREATE TABLE IF NOT EXISTS teams (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    team_code VARCHAR(10) NOT NULL UNIQUE,
    create_date DATE NOT NULL,
    creator_id UUID NOT NULL,
    total_members INT DEFAULT 1 NOT NULL,
    avatar_url VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS team_user (
    id UUID PRIMARY KEY,
    team_id UUID NOT NULL,
    user_id UUID NOT NULL,
    join_date DATE NOT NULL,
    role VARCHAR(20) NOT NULL,
    CONSTRAINT unique_team_user UNIQUE (user_id, team_id)
);