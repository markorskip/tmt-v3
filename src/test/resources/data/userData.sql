INSERT INTO users (
    email,
    password,
    enabled,
    nickname,
    date_created,
    last_updated
) VALUES (
    'bootify',
    '{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6',
    TRUE,
    'Lorem ipsum dolor.',
    '2023-09-02 16:30:00',
    '2023-09-02 16:30:00'
);

INSERT INTO users (
    email,
    password,
    enabled,
    nickname,
    date_created,
    last_updated
) VALUES (
    'bootify1',
    '{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6',
    FALSE,
    'Duis autem vel.',
    '2023-09-03 16:30:00',
    '2023-09-03 16:30:00'
);
