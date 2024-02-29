USE TICKET_DEV;

CREATE TABLE USER_CARD
(
    ID             INT(10)      NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique identifier',
    CARD_ID        VARCHAR(10)  NOT NULL COMMENT 'User''s card ID. References the Core API''s BANK_CARD.CARD_ID',
    USER_ID        INT(10)      NOT NULL COMMENT 'User''s unique identifier. References the user in the core module'
);

CREATE TABLE RESERVATION
(
    ID             INT(10)      NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique identifier',
    EVENT_ID       INT(10)      NOT NULL COMMENT 'Event ID. References the Partner''s event.',
    SEAT_CODE      VARCHAR(10)  NOT NULL COMMENT 'Seat code for the event. References the Partner''s seat',
    USER_ID        INT(10)      NOT NULL COMMENT 'User''s unique identifier. References the user in the core module',
    TRANSACTION_ID VARCHAR(200) NOT NULL UNIQUE COMMENT 'An OTP Mobil system-wide ID for payment transaction.', -- NOTE: that would be great if the partners also would handle it. great help for debugging and reverse searching
    STATUS         VARCHAR(20)  NOT NULL COMMENT 'Current reservation status - for feature features, e.g. cancellation, archive',
    INDEX IDX_ID (ID),
    INDEX IDX_USER_ID (USER_ID),
    UNIQUE KEY UQ_EVENT_SEAT (EVENT_ID, SEAT_CODE) -- NOTE: this can be used if and ONLY IF there is exactly 1 partner system, in the case of several systems the restriction must be extended to several columns
);

CREATE TABLE RESERVATION_LOCK
(
    ID             INT(10)      NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique identifier',
    EVENT_ID       INT(10)      NOT NULL COMMENT 'Event ID. References the Partner''s event.',
    USER_ID        INT(10)      NOT NULL COMMENT 'User''s unique identifier. References the user in the core module',
    TRANSACTION_ID VARCHAR(200) NOT NULL UNIQUE COMMENT 'An OTP Mobil system-wide ID for payment transaction.',
    CREATED_AT     TIMESTAMP    NOT NULL DEFAULT NOW() COMMENT 'Lock creation timestamp',
    INDEX IDX_EVENT_USER (EVENT_ID, USER_ID),
    INDEX IDX_TRANSACTION (TRANSACTION_ID),
    UNIQUE KEY UQ_EVENT_USER (EVENT_ID, USER_ID) -- NOTE: fails the lock insert if the user wants to reserve seats parallel for an event
)