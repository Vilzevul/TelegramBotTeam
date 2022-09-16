-- liquibase formatted sql

-- changeSet alexadler:1

-- создать таблицу "Пользователи"
CREATE TABLE users
(
    id        bigint       PRIMARY KEY,
    name      text         NOT NULL,
    phone     varchar(32),
    role      varchar(32)  NOT NULL DEFAULT 'USER'
);

-- создать таблицу "Усыновления"
CREATE TABLE adoptions
(
    id           bigserial    PRIMARY KEY,
    id_parent    bigint       REFERENCES users (id),
    id_volunteer bigint       REFERENCES users (id),
    start_date   date         NOT NULL,
    end_date     date         NOT NULL CHECK (end_date > start_date),
    status       varchar(32)  NOT NULL DEFAULT 'ACTIVE'
);

-- создать таблицу "Отчеты"
CREATE TABLE reports
(
    id              bigserial PRIMARY KEY,
    id_adoption     bigint    REFERENCES adoptions (id),
    report_date     date      NOT NULL,
    report_image    bytea,
    report_message  text
);

-- changeSet alexadler:2

-- создать таблицу "Приюты"
CREATE TABLE shelters
(
    id   bigserial    PRIMARY KEY,
    name varchar(32)  NOT NULL UNIQUE
);

-- создать в таблице shelters приюты для собак и кошек
INSERT INTO shelters (name)
VALUES ('DOGS'),
       ('CATS');

-- создать таблицу "Участники приюта"
CREATE TABLE members
(
    id         bigserial    PRIMARY KEY,
    id_user    bigint       REFERENCES users (id),
    id_shelter bigint       REFERENCES shelters (id),
    role       varchar(32)  NOT NULL DEFAULT 'USER'
);

-- перенести в таблицу members текущих пользователей с их ролями
INSERT INTO members(id_user, role)
(SELECT id, role FROM users);

-- установить для members приют DOGS по умолчанию
UPDATE members
SET id_shelter = (SELECT id FROM shelters WHERE name = 'DOGS')
WHERE id_shelter IS NULL;

-- удалить роль из таблицы users, теперь она определена в таблице members
ALTER TABLE users
DROP COLUMN IF EXISTS role;

-- изменить внешние ключи таблицы adoptions: теперь id_parent и id_volunteer должны ссылаться на members

-- создать временную таблицу для хранения значений старых внешних ключей таблицы adoptions
CREATE TEMP TABLE adoptions_keys_temp AS
SELECT id, id_parent, id_volunteer
FROM adoptions;

-- сбросить старые внешние ключи
UPDATE adoptions
SET (id_parent, id_volunteer) = (NULL, NULL);

ALTER TABLE adoptions
DROP CONSTRAINT adoptions_id_parent_fkey;

ALTER TABLE adoptions
ADD CONSTRAINT adoptions_id_parent_fkey FOREIGN KEY (id_parent) REFERENCES members(id);

UPDATE adoptions a
SET id_parent = (SELECT m.id FROM members m, adoptions_keys_temp t WHERE a.id = t.id AND m.id_user = t.id_parent);

ALTER TABLE adoptions
DROP CONSTRAINT adoptions_id_volunteer_fkey;

ALTER TABLE adoptions
ADD CONSTRAINT adoptions_id_volunteer_fkey FOREIGN KEY (id_volunteer) REFERENCES members(id);

UPDATE adoptions a
SET id_volunteer = (SELECT m.id FROM members m, adoptions_keys_temp t WHERE a.id = t.id AND m.id_user = t.id_volunteer);

-- удалить временную таблицу
DROP TABLE adoptions_keys_temp;
