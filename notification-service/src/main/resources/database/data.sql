INSERT INTO notifications (
    id, user_id, title, content, created_at, is_read, subject, subject_id
) VALUES
      ('11111111-1111-1111-1111-111111111111', '041c77e0-ae77-4005-b745-ea12dca9bec6',
       'Plan reminded', 'Plan "Learn Spring Boot" will expire at 10:00:00 on 12/02/2025',
       NOW() - INTERVAL '1 day', false, 'PLAN', '11111111-1111-1111-1111-111111111111'),

      ('22222222-2222-2222-2222-222222222222', '618c10ee-923f-4323-b32b-086caa534b46',
       'Team updated', 'Adam Lambert has updated the team "TEAM 01"''s general information.',
       NOW() - INTERVAL '2 hours', false, 'TEAM', '111e8400-e29b-41d4-a716-446655440001');

INSERT INTO invitations (
    id, inviter_name, inviter_avatar_url, invitee_id, team_id, team_name, invited_at
) VALUES
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'David Becker', 'https://example.com/avatar/john.png',
     '041c77e0-ae77-4005-b745-ea12dca9bec6', '555e8400-e29b-41d4-a716-446655440006',
     'TEAM 02', NOW() - INTERVAL '1 day');

INSERT INTO device_tokens (
    id, user_id, fcm_token, last_updated
) VALUES
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '041c77e0-ae77-4005-b745-ea12dca9bec6',
     'fcm_token_123_example_xyz', NOW() - INTERVAL '10 minutes');
