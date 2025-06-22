INSERT INTO teams (id, name, description, team_code, create_date, creator_id, total_members, avatar_url)
VALUES
    ('111e8400-e29b-41d4-a716-446655440001', 'Biology', '','AQhTe',
     '2024-12-01','041c77e0-ae77-4005-b745-ea12dca9bec6', 2, 'https://res.cloudinary.com/drvyagz4w/image/upload/v1750258950/111e8400-e29b-41d4-a716-446655440001.jpg'),
    ('555e8400-e29b-41d4-a716-446655440006', 'Software devs', 'demo', 'fDjQA',
     '2024-07-08', '9f5d79f2-83a2-411d-bb66-caeb640a62b0', 1, 'https://res.cloudinary.com/drvyagz4w/image/upload/v1750258952/555e8400-e29b-41d4-a716-446655440006.jpg');

INSERT INTO team_user (id, team_id, user_id, join_date, role)
VALUES
    ('550e8400-e29b-41d4-a716-446655440000', '111e8400-e29b-41d4-a716-446655440001', '041c77e0-ae77-4005-b745-ea12dca9bec6', '2024-12-01', 'CREATOR'),
    ('660e8400-e29b-41d4-a716-446655440003', '111e8400-e29b-41d4-a716-446655440001', '618c10ee-923f-4323-b32b-086caa534b46', '2024-12-05', 'MEMBER'),
    ('770e8400-e29b-41d4-a716-446655440005', '555e8400-e29b-41d4-a716-446655440006', '9f5d79f2-83a2-411d-bb66-caeb640a62b0', '2024-07-08', 'CREATOR');
