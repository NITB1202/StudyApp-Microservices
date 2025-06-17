CREATE TABLE sessions (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    study_date TIMESTAMP NOT NULL,
    duration_in_minutes INTEGER NOT NULL,
    elapsed_time_in_minutes INTEGER NOT NULL
);
