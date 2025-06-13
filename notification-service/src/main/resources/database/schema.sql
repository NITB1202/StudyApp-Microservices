CREATE TABLE notifications (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    is_read BOOLEAN NOT NULL,
    subject VARCHAR(50),
    subject_id UUID
);

CREATE TABLE invitations (
    id UUID PRIMARY KEY,
    inviter_name VARCHAR(100) NOT NULL,
    inviter_avatar_url TEXT NOT NULL,
    invitee_id UUID NOT NULL,
    team_id UUID NOT NULL,
    team_name VARCHAR(100) NOT NULL,
    invited_at TIMESTAMP NOT NULL
);

CREATE TABLE device_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    fcm_token TEXT NOT NULL,
    last_updated TIMESTAMP NOT NULL
);
