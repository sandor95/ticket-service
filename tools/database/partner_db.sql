USE PARTNER;

CREATE TABLE EVENT
(
    ID         INT(10)      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    TITLE      VARCHAR(200) NOT NULL,
    LOCATION   VARCHAR(100) NOT NULL,
    START_TIME TIMESTAMP    NOT NULL,
    END_TIME   TIMESTAMP    NOT NULL,
    INDEX IDX_ID (TITLE)
);

CREATE TABLE TICKET
(
    ID             INT(10)      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    PRICE          INTEGER      NOT NULL,
    CURRENCY       VARCHAR(255) NOT NULL,
    RESERVATION_ID INT(10)      UNIQUE,
    EVENT_ID       INT(10)      NOT NULL,
    SEAT_ID        INT(10)      NOT NULL,
    INDEX IDX_ID (ID)
);

CREATE TABLE SEAT
(
    ID        INT(10) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    SEAT_CODE VARCHAR(10),
    INDEX idx_seat_code (SEAT_CODE)
);


ALTER TABLE TICKET
    ADD FOREIGN KEY (EVENT_ID) REFERENCES EVENT (ID);

ALTER TABLE TICKET
    ADD FOREIGN KEY (SEAT_ID) REFERENCES SEAT (ID);


-- DATA SEEDING

INSERT INTO EVENT (TITLE, LOCATION, START_TIME, END_TIME)
VALUES ('Szilveszteri zártkörű rendezvény', 'Greenwich', FROM_UNIXTIME(1577836800), FROM_UNIXTIME(1577844000)),
       ('Májusi mulatság', 'Budapest', FROM_UNIXTIME(1588334400), FROM_UNIXTIME(1588348800)),
       ('Necc party', 'Debrecen', FROM_UNIXTIME(1607731200), FROM_UNIXTIME(1607817599)),
       ('Esti Kornél - Itt maradtam a Budapest Parkban', 'Budapest', FROM_UNIXTIME(1715270400), FROM_UNIXTIME(1715277600)),
       ('Bagossy Brothers Company', 'Szeged', FROM_UNIXTIME(1714834800), FROM_UNIXTIME(1714845600));

INSERT INTO SEAT (SEAT_CODE)
VALUES ('S1'),
       ('S2'),
       ('S3'),
       ('S4'),
       ('S5'),
       ('S6'),
       ('S7'),
       ('S8'),
       ('S9'),
       ('S10');

INSERT INTO TICKET (PRICE, CURRENCY, RESERVATION_ID, EVENT_ID, SEAT_ID)
VALUES (1000, 'HUF', 2, (SELECT ID FROM EVENT WHERE TITLE = 'Szilveszteri zártkörű rendezvény'), 1),
       (1000, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Szilveszteri zártkörű rendezvény'), 2),
       (1000, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Szilveszteri zártkörű rendezvény'), 3),
       (1000, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Szilveszteri zártkörű rendezvény'), 4),
       (1000, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Szilveszteri zártkörű rendezvény'), 5),
       (1000, 'HUF', (SELECT ID FROM EVENT WHERE TITLE = 'Szilveszteri zártkörű rendezvény'), 1, 6),
       (1000, 'HUF', 3, (SELECT ID FROM EVENT WHERE TITLE = 'Szilveszteri zártkörű rendezvény'), 7),
       (1000, 'HUF', 4, (SELECT ID FROM EVENT WHERE TITLE = 'Szilveszteri zártkörű rendezvény'), 8),
       (1000, 'HUF', 10, (SELECT ID FROM EVENT WHERE TITLE = 'Szilveszteri zártkörű rendezvény'), 9),
       (1000, 'HUF', 20, (SELECT ID FROM EVENT WHERE TITLE = 'Szilveszteri zártkörű rendezvény'), 10),

       (2000, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Májusi mulatság'), 1),
       (2000, 'HUF', 21, (SELECT ID FROM EVENT WHERE TITLE = 'Májusi mulatság'), 2),
       (2000, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Májusi mulatság'), 3),
       (2000, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Májusi mulatság'), 4),
       (2000, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Májusi mulatság'), 5),
       (2000, 'HUF', 24, (SELECT ID FROM EVENT WHERE TITLE = 'Májusi mulatság'), 6),
       (2000, 'HUF', 27, (SELECT ID FROM EVENT WHERE TITLE = 'Májusi mulatság'), 7),
       (2000, 'HUF', 31, (SELECT ID FROM EVENT WHERE TITLE = 'Májusi mulatság'), 8),
       (2000, 'HUF', 36, (SELECT ID FROM EVENT WHERE TITLE = 'Májusi mulatság'), 9),
       (2000, 'HUF', 40, (SELECT ID FROM EVENT WHERE TITLE = 'Májusi mulatság'), 10),

       (3000, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Necc party'), 1),
       (3000, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Necc party'), 2),
       (3000, 'HUF', 44, (SELECT ID FROM EVENT WHERE TITLE = 'Necc party'), 3),
       (3000, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Necc party'), 4),
       (3000, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Necc party'), 5),
       (3000, 'HUF', 48, (SELECT ID FROM EVENT WHERE TITLE = 'Necc party'), 6),
       (3000, 'HUF', 52, (SELECT ID FROM EVENT WHERE TITLE = 'Necc party'), 7),
       (3000, 'HUF', 55, (SELECT ID FROM EVENT WHERE TITLE = 'Necc party'), 8),
       (3000, 'HUF', 60, (SELECT ID FROM EVENT WHERE TITLE = 'Necc party'), 9),
       (3000, 'HUF', 63, (SELECT ID FROM EVENT WHERE TITLE = 'Necc party'), 10),

       (4500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Esti Kornél - Itt maradtam a Budapest Parkban'), 1),
       (4500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Esti Kornél - Itt maradtam a Budapest Parkban'), 2),
       (4500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Esti Kornél - Itt maradtam a Budapest Parkban'), 3),
       (4500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Esti Kornél - Itt maradtam a Budapest Parkban'), 4),
       (4500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Esti Kornél - Itt maradtam a Budapest Parkban'), 5),
       (4500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Esti Kornél - Itt maradtam a Budapest Parkban'), 6),
       (4500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Esti Kornél - Itt maradtam a Budapest Parkban'), 7),
       (4500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Esti Kornél - Itt maradtam a Budapest Parkban'), 8),
       (4500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Esti Kornél - Itt maradtam a Budapest Parkban'), 9),
       (4500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Esti Kornél - Itt maradtam a Budapest Parkban'), 10),

       (1500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Bagossy Brothers Company'), 1),
       (1500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Bagossy Brothers Company'), 2),
       (1500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Bagossy Brothers Company'), 3),
       (1500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Bagossy Brothers Company'), 4),
       (1500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Bagossy Brothers Company'), 5),
       (1500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Bagossy Brothers Company'), 6),
       (1500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Bagossy Brothers Company'), 7),
       (1500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Bagossy Brothers Company'), 8),
       (1500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Bagossy Brothers Company'), 9),
       (1500, 'HUF', NULL, (SELECT ID FROM EVENT WHERE TITLE = 'Bagossy Brothers Company'), 10);