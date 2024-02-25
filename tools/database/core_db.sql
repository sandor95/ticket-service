USE CORE_DEV;

CREATE TABLE USER
(
    ID    INT(10)      NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique identifier',
    NAME  VARCHAR(200) NOT NULL COMMENT 'User''s full name',
    EMAIL VARCHAR(200) NOT NULL UNIQUE COMMENT 'User''s e-mail address',
    INDEX IDX_ID (ID),
    INDEX IDX_NAME_EMAIL (NAME, EMAIL)
);

CREATE TABLE DEVICE
(
    ID          INT(10)    NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique identifier',
    USER_ID     INT(10)    NOT NULL COMMENT 'User''s unique identifier',
    DEVICE_HASH MEDIUMTEXT NOT NULL COMMENT 'Raw hash value'
);

CREATE TABLE TOKEN
(
    ID       INT(10)    NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique identifier',
    USER_ID  INT(10)    NOT NULL COMMENT 'User''s unique identifier',
    TOKEN    MEDIUMTEXT NOT NULL COMMENT 'Raw token value', -- can this be UNIQUE? it seems it could (if it would be JWT (e.g. SSO token) than is must be unique and it would be stored in a Redis)
    VALID_TO TIMESTAMP  NOT NULL COMMENT 'Token expiration timestamp'
);

CREATE TABLE BANK_CARD
(
    ID          INT(10)      NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'Unique identifier',
    USER_ID     INT(10)      NOT NULL COMMENT 'User''s unique identifier',
    CARD_ID     VARCHAR(10)  NOT NULL UNIQUE COMMENT 'Card''s unique identifier',
    CARD_NUMBER VARCHAR(200) NOT NULL UNIQUE COMMENT 'Number of the bank card',
    CVC         SMALLINT(3)  NOT NULL COMMENT 'Card Verification Code',
    NAME        VARCHAR(200) NOT NULL COMMENT 'Owner of the bank card',
    VALID_TO    DATE         NOT NULL COMMENT 'Expiration month of the bank card. It stores the 1st day of the last valid month!
                                                For example: 2022-02-01, it means the card valid till 2022-02-28 and invalid from 2022-03-01.',
    AMOUNT      INT(10)      NOT NULL COMMENT 'Actual amount',
    CURRENCY    VARCHAR(255) NOT NULL COMMENT 'Currency of the card'
);


ALTER TABLE DEVICE
    ADD FOREIGN KEY (USER_ID) REFERENCES USER (ID);

ALTER TABLE TOKEN
    ADD FOREIGN KEY (USER_ID) REFERENCES USER (ID);

ALTER TABLE BANK_CARD
    ADD FOREIGN KEY (USER_ID) REFERENCES USER (ID);


-- DATA SEEDING

INSERT INTO USER (ID, NAME, EMAIL)
VALUES (1000, 'Teszt Aladár', 'teszt.aladar@otpmobil.com'),
       (2000, 'Teszt Benedek', 'teszt.benedek@otpmobil.com'),
       (3000, 'Teszt Cecília', 'teszt.cecilia@otpmobil.com'),
       (12000, 'Teszt Dömötör', 'teszt.domotor@otpmobil.com'),
       (20000, 'Teszt Erika', 'teszt.erika@otpmobil.com'),
       (5500, 'Űrményi-Vőfély Örs Üllő', 'inkabb.hagyjuk-mi-a-teljes.neve@otpmobil.com');


INSERT INTO DEVICE (USER_ID, DEVICE_HASH)
VALUES (1000, 'F67C2BCBFCFA30FCCB36F72DCA22A817'),
       (1000, '0F1674BD19D3BBDD4C39E14734FFB876'),
       (1000, '3AE5E9658FBD7D4048BD40820B7D227D'),
       (2000, 'FADDFEA562F3C914DCC81956682DB0FC'),
       (3000, 'E68560872BDB2DF2FFE7ADC091755378'),
       (12000, 'LBUW70B0H9ERZZY1PITHOE2U72WU9XYU'),
       (20000, 'DMPAGOLOQZ7JPXWDKMJ3LEWVEV2V316Q'),
       (5500, 'CYPA0VARI2YMWZH0ZZFDGX3FVZFN3TZG');


INSERT INTO TOKEN (USER_ID, TOKEN, VALID_TO)
VALUES (1000, 'dGVzenQuYWxhZGFyQG90cG1vYmlsLmNvbSYxMDAwJkY2N0MyQkNCRkNGQTMwRkNDQjM2RjcyRENBMjJBODE3', NOW() + INTERVAL 90 MINUTE),
       (2000, 'dGVzenQuYmVuZWRla0BvdHBtb2JpbC5jb20mMjAwMCZGQURERkVBNTYyRjNDOTE0RENDODE5NTY2ODJEQjBGQw==', NOW() + INTERVAL 90 MINUTE),
       (3000, 'dGVzenQuY2VjaWxpYUBvdHBtb2JpbC5jb20mMzAwMCZFNjg1NjA4NzJCREIyREYyRkZFN0FEQzA5MTc1NTM3OA==', NOW() + INTERVAL 90 MINUTE),
       (1000, 'dGVzenQuYWxhZGFyQG90cG1vYmlsLmNvbSYxMDAwJjBGMTY3NEJEMTlEM0JCREQ0QzM5RTE0NzM0RkZCODc2', NOW() + INTERVAL 90 MINUTE),
       (1000, 'dGVzenQuYWxhZGFyQG90cG1vYmlsLmNvbSYxMDAwJjNBRTVFOTY1OEZCRDdENDA0OEJENDA4MjBCN0QyMjdE', NOW() + INTERVAL 90 MINUTE),
       (12000, 'dGVzenQuZG9tb3RvckBvdHBtb2JpbC5jb20mMTIwMDAmTEJVVzcwQjBIOUVSWlpZMVBJVEhPRTJVNzJXVTlYWVU=', NOW() + INTERVAL 90 MINUTE),
       (20000, 'dGVzenQuZXJpa2FAb3RwbW9iaWwuY29tJjIwMDAwJkRNUEFHT0xPUVo3SlBYV0RLTUozTEVXVkVWMlYzMTZR', NOW() + INTERVAL 90 MINUTE),
       (5500, 'aW5rYWJiLmhhZ3lqdWstbWktYS10ZWxqZXMubmV2ZUBvdHBtb2JpbC5jb20mNTUwMCZDWVBBMFZBUkkyWU1XWkgwWlpGREdYM0ZWWkZOM1RaRw==', NOW() + INTERVAL 90 MINUTE);


INSERT INTO BANK_CARD (USER_ID, CARD_ID, CARD_NUMBER, CVC, NAME, VALID_TO, AMOUNT, CURRENCY)
VALUES (1000, 'C0001', '5299706965433676', 123, '', STR_TO_DATE('2024-08-01', '%Y-%m-%d'), 1000, 'HUF'),
       (2000, 'C0002', '5390508354245119', 456, '', STR_TO_DATE('2024-09-01', '%Y-%m-%d'), 2000, 'HUF'),
       (3000, 'C0003', '4929088924014470', 789, '', STR_TO_DATE('2024-07-01', '%Y-%m-%d'), 3000, 'HUF'),
       (12000, 'C0004', '3891053918054353', 145, '', STR_TO_DATE('2024-05-01', '%Y-%m-%d'), 16000, 'HUF'),
       (20000, 'C0005', '2755799361195543', 956, '', STR_TO_DATE('2023-12-01', '%Y-%m-%d'), 22000, 'HUF'),
       (5500, 'C0006', '2675682222828966', 043, '', STR_TO_DATE('2024-02-01', '%Y-%m-%d'), 234678, 'HUF');