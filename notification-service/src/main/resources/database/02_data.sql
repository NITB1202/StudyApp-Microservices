INSERT INTO notifications (
    id, user_id, title, content, created_at, is_read, subject, subject_id
) VALUES
      ('11111111-1111-1111-1111-111111111111', '041c77e0-ae77-4005-b745-ea12dca9bec6',
       'Plan reminded', 'Plan "Learn Spring Boot" will expire at 10:00:00 on 12/02/2025',
       NOW(), false, 'PLAN', '11111111-1111-1111-1111-111111111111'),

      ('22222222-2222-2222-2222-222222222222', '618c10ee-923f-4323-b32b-086caa534b46',
       'Team updated', 'Adam Lambert has updated the team "TEAM 01"''s general information.',
       NOW(), false, 'TEAM', '111e8400-e29b-41d4-a716-446655440001');

INSERT INTO invitations (
    id, inviter_name, inviter_avatar_url, invitee_id, team_id, team_name, invited_at
) VALUES
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'David Becker', 'https://example.com/avatar/john.png',
     '041c77e0-ae77-4005-b745-ea12dca9bec6', '555e8400-e29b-41d4-a716-446655440006',
     'TEAM 02', NOW());

INSERT INTO device_tokens (
    id, user_id, fcm_token, last_updated
) VALUES
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '9f5d79f2-83a2-411d-bb66-caeb640a62b0',
     'fPosfcSXTKCtb1agcJ8tHI:APA91bFRp_1U_lEnlXMGrpgqR1FwfQF61JYDV43d1qonzumIQvzuX_IHgTCGZUjI0RBXCRToJOpZ3RaA_O5dndYFfFM1v8aNdgnQaw6KiRYPY3etPrJKngc', NOW());

INSERT INTO team_notification_settings (
    id, user_id, team_id, team_notification, team_plan_reminder, chat_notification
) VALUES
      ('10000000-0000-0000-0000-000000000001', '041c77e0-ae77-4005-b745-ea12dca9bec6', '111e8400-e29b-41d4-a716-446655440001', true, true, true),
      ('10000000-0000-0000-0000-000000000002', '618c10ee-923f-4323-b32b-086caa534b46', '111e8400-e29b-41d4-a716-446655440001', true, false, true),
      ('10000000-0000-0000-0000-000000000003', '9f5d79f2-83a2-411d-bb66-caeb640a62b0', '555e8400-e29b-41d4-a716-446655440006', false, false, false);
