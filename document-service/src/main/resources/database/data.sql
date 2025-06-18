INSERT INTO user_usage (user_id, used, total) VALUES
    ('041c77e0-ae77-4005-b745-ea12dca9bec6', 9256, 2147483648),
    ('618c10ee-923f-4323-b32b-086caa534b46', 0, 2147483648),
    ('9f5d79f2-83a2-411d-bb66-caeb640a62b0', 0, 2147483648);

INSERT INTO team_usage (team_id, used, total) VALUES
    ('111e8400-e29b-41d4-a716-446655440001', 10598, 4294967296),
    ('555e8400-e29b-41d4-a716-446655440006', 0, 4294967296);

INSERT INTO folders (id, name, created_by, created_at, updated_by, updated_at, bytes, document_count, team_id) VALUES
    ('f1f1f1f1-f1f1-f1f1-f1f1-f1f1f1f1f1f1', 'Personal Notes', '041c77e0-ae77-4005-b745-ea12dca9bec6', NOW(), '041c77e0-ae77-4005-b745-ea12dca9bec6', NOW(), 104857600, 1, NULL),
    ('f2f2f2f2-f2f2-f2f2-f2f2-f2f2f2f2f2f2', 'Team Docs', '041c77e0-ae77-4005-b745-ea12dca9bec6', NOW(), '041c77e0-ae77-4005-b745-ea12dca9bec6', NOW(), 524288000, 1, '111e8400-e29b-41d4-a716-446655440001');

INSERT INTO documents (id, folder_id, name, created_by, created_at, updated_by, updated_at, bytes, url) VALUES
    ('d1d1d1d1-d1d1-d1d1-d1d1-d1d1d1d1d1d1', 'f1f1f1f1-f1f1-f1f1-f1f1-f1f1f1f1f1f1', 'Test.xlsx', '041c77e0-ae77-4005-b745-ea12dca9bec6', NOW(), '041c77e0-ae77-4005-b745-ea12dca9bec6', NOW(), 1048576, 'https://res.cloudinary.com/drvyagz4w/raw/upload/v1750259176/d1d1d1d1-d1d1-d1d1-d1d1-d1d1d1d1d1d1'),
    ('d2d2d2d2-d2d2-d2d2-d2d2-d2d2d2d2d2d2', 'f2f2f2f2-f2f2-f2f2-f2f2-f2f2f2f2f2f2', 'TeamPlan.xlsx', '618c10ee-923f-4323-b32b-086caa534b46', NOW(), '618c10ee-923f-4323-b32b-086caa534b46', NOW(), 5242880, 'https://res.cloudinary.com/drvyagz4w/raw/upload/v1750259324/d2d2d2d2-d2d2-d2d2-d2d2-d2d2d2d2d2d2');
