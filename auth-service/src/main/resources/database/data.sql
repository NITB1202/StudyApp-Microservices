INSERT INTO accounts (id, user_id, provider, provider_id, email, hashed_password, role)
VALUES
    -- password: admin123
    (
        '2e4a1e62-2ed9-4f12-9d36-1a9b1e1d1234',
        '041c77e0-ae77-4005-b745-ea12dca9bec6',
        'LOCAL',
        NULL,
        'user1@english4u.com',
        '$2a$10$BKfmTWy1Sq4NWfdXHa.08O1dMZke1Kl72tPWCJ4FGNIjFz/CxxbDu',
        'USER'
    ),
    -- password: user1234
    (
        '3e1a2b13-8fd4-40ae-81b5-1ec97a5b6789',
        '618c10ee-923f-4323-b32b-086caa534b46',
        'LOCAL',
        NULL,
        'user2@english4u.com',
        '$2a$10$zpygZ72GKBRwd8PNiHcRMO5muGxZr/YfJ.V4Y2sCfai7CP76wpxHC',
        'USER'
    ),
    -- password: system123
    (
        '7a93f819-daa7-4d13-bc4f-e67f6e452aaa',
        '9f5d79f2-83a2-411d-bb66-caeb640a62b0',
        'LOCAL',
        NULL,
        'user3@english4u.com',
        '$2a$10$/j.77sEIl8aJBjOSj1G6.erGDyAE19Z2BsRcPABkhgb8GNfiwp6cO',
        'USER'
    );
