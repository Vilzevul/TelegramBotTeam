--Создание таблицы user "Пользователи"
CREATE TABLE users
(
    id        bigint       PRIMARY KEY,
    name      text         NOT NULL,
    phone     varchar(32),
    role      varchar(32)  NOT NULL DEFAULT 'USER'
);

--Создание таблицы adoptions "Усыновления"
CREATE TABLE adoptions
(
    id           bigserial    PRIMARY KEY,
    id_parent    bigint       REFERENCES users (id),
    id_volunteer bigint       REFERENCES users (id),
    start_date   date         NOT NULL,
    end_date     date         NOT NULL CHECK (end_date > start_date),
    status       varchar(32)  NOT NULL DEFAULT 'ACTIVE'
);

--Создание таблицы report "Отчет"
CREATE TABLE reports
(
    id              bigserial PRIMARY KEY,
    id_adoption     bigint    REFERENCES adoptions (id),
    report_date     date      NOT NULL,
    report_image    bytea,
    report_message  text
);