INSERT INTO messages (id, team_id, user_id, content, created_at, image_url, is_deleted)
VALUES
    ('44444444-4444-4444-4444-444444444444', '111e8400-e29b-41d4-a716-446655440001', '618c10ee-923f-4323-b32b-086caa534b46', 'First message.', NOW(), NULL, FALSE),
    ('55555555-5555-5555-5555-555555555555', '111e8400-e29b-41d4-a716-446655440001', '618c10ee-923f-4323-b32b-086caa534b46', '',   DATEADD(SECOND, 5, CURRENT_TIMESTAMP), 'https://example.com/image.jpg', FALSE);

INSERT INTO message_read_status (id, message_id, user_id, read_at)
VALUES
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', '44444444-4444-4444-4444-444444444444', '041c77e0-ae77-4005-b745-ea12dca9bec6', NOW());
