CREATE TABLE plans (
    id UUID PRIMARY KEY,
    creator_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_at TIMESTAMP NOT NULL,
    end_at TIMESTAMP NOT NULL,
    progress FLOAT NOT NULL,
    complete_at TIMESTAMP,
    team_id UUID
);

CREATE TABLE tasks (
    id UUID PRIMARY KEY,
    plan_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    assignee_id UUID NOT NULL,
    is_completed BOOLEAN NOT NULL
);

CREATE TABLE plan_reminders (
    id UUID PRIMARY KEY,
    plan_id UUID NOT NULL,
    receiver_ids VARCHAR(500) NOT NULL,
    remind_at TIMESTAMP NOT NULL
);


