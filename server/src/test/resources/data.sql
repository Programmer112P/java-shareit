INSERT INTO users (name, email)
VALUES ('user1', 'user1@email.com'),
       ('user2', 'user2@email.com'),
       ('user3', 'user3@email.com');

INSERT INTO request (DESCRIPTION, created, REQUESTOR_ID)
VALUES ('beze', '2022-10-10 11:30:30', 1);

INSERT INTO ITEMS (NAME, DESCRIPTION, IS_AVAILABLE, OWNER_ID, REQUEST_ID)
VALUES ('item1_name', 'item1_description', true, 1, 1),
       ('item2_name', 'item2_description', false, 1, null),
       ('item3_name', 'item3_description', true, 3, null);

INSERT INTO BOOKINGS (START_DATE, END_DATE, ITEM_ID, BOOKER_ID, STATUS)
VALUES ('2021-10-10 11:30:30', '2021-10-12 11:30:30', 1, 3, 'WAITING'),
       ('2022-10-10 11:30:30', '2022-10-12 11:30:30', 1, 2, 'REJECTED'),
       ('2022-01-01 11:30:30', '2022-01-10 11:30:30', 3, 1, 'APPROVED');

INSERT INTO COMMENTS (TEXT, ITEM_ID, AUTHOR_ID, CREATED)
VALUES ('item1_nice', 3, 1, '2023-01-01 11:30:30');