CREATE TABLE messages (
    id UUID PRIMARY KEY,
    team_id UUID NOT NULL,
    user_id UUID NOT NULL,
    content TEXT,
    created_at TIMESTAMP NOT NULL,
    image_url TEXT,
    is_deleted BOOLEAN NOT NULL
);

CREATE TABLE message_read_status (
    id UUID PRIMARY KEY,
    message_id UUID NOT NULL,
    user_id UUID NOT NULL,
    read_at TIMESTAMP NOT NULL
);

CREATE TABLE chat_notifications (
    team_id UUID PRIMARY KEY,
    team_name VARCHAR(255) NOT NULL,
    new_message_count INTEGER NOT NULL
);
