--Создание таблицы user "Пользователи"
CREATE TABLE users
(
    idUser   bigserial PRIMARY KEY,
    nameUser TEXT    NOT NULL,
    phone    varchar(36),
    role     integer NOT NULL
);

--Создание таблицы report "Отчет"
CREATE TABLE report
(
    idReport         bigserial PRIMARY KEY,
    idAdoptiveParent bigserial REFERENCES adoptiveParent (idAdoptiveParent),
    report_date      date NOT NULL,
    report_image     bytea,
    report_message   TEXT
);

--Создание таблицы adoptive_parent "Усыновители"
CREATE TABLE adoptiveParent
(
    idAdoptiveParent bigserial PRIMARY KEY,
    idVolunteer      bigserial REFERENCES users (idUser),
    start_date       date    NOT NULL,
    end_date         date    NOT NULL CHECK (end_date > start_date),
    status           integer NOT NULL
);








