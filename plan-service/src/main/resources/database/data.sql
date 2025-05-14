INSERT INTO plans (
    id, creator_id, name, description, start_at, end_at, progress, complete_at, team_id
) VALUES
      (
          '11111111-1111-1111-1111-111111111111',
          '041c77e0-ae77-4005-b745-ea12dca9bec6',
          'Learn Spring Boot',
          'A study plan to master Spring Boot in 30 days.',
          '2025-05-01 08:00:00',
          '2025-05-31 23:59:59',
          0.5,
          NULL,
          '111e8400-e29b-41d4-a716-446655440001'
      ),
      (
          '22222222-2222-2222-2222-222222222222',
          '041c77e0-ae77-4005-b745-ea12dca9bec6',
          'Build Microservices',
          'Plan to build a microservice system using gRPC.',
          '2025-05-05 09:00:00',
          '2025-06-10 18:00:00',
          1,
          '2025-05-25 16:00:00',
          NULL
      );

INSERT INTO tasks (
    id, plan_id, name, assignee_id, is_completed
) VALUES
      (
          'aaaa1111-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
          '11111111-1111-1111-1111-111111111111',
          'Read Spring Boot Docs',
          '041c77e0-ae77-4005-b745-ea12dca9bec6',
          false
      ),
      (
          'bbbb2222-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
          '11111111-1111-1111-1111-111111111111',
          'Build a demo project',
          '618c10ee-923f-4323-b32b-086caa534b46',
          true
      ),
      (
          'cccc3333-cccc-cccc-cccc-cccccccccccc',
          '22222222-2222-2222-2222-222222222222',
          'Setup gRPC Gateway',
          '041c77e0-ae77-4005-b745-ea12dca9bec6',
          true
      );

INSERT INTO plan_reminders (
    id, plan_id, receiver_ids, remind_at
) VALUES
      (
          'dddd1111-dddd-dddd-dddd-dddddddddddd',
          '11111111-1111-1111-1111-111111111111',
          '041c77e0-ae77-4005-b745-ea12dca9bec6,618c10ee-923f-4323-b32b-086caa534b46',
          '2025-05-15 07:00:00'
      ),
      (
          'eeee2222-eeee-eeee-eeee-eeeeeeeeeeee',
          '22222222-2222-2222-2222-222222222222',
          '041c77e0-ae77-4005-b745-ea12dca9bec6',
          '2025-05-20 08:00:00'
      );
