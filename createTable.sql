--Создание таблицы user "Пользователи"
CREATE TABLE users
(
    idChat    bigint PRIMARY KEY,
    nameUser  varchar(36) NOT NULL,
    phone     integer,
    role      integer,
    report_Id integer REFERENCES report (id)
);

--Создание таблицы report "Отчет"
CREATE TABLE report
(
    id             integer PRIMARY KEY,
    idUser         integer NOT NULL,
    report_date    datetime,
    report_image   bytea,
    report_message varchar(36)
);

--Создание таблицы adoptive_parent "Усыновители"
CREATE TABLE adoptiveParent
(
    id           integer PRIMARY KEY,
    idUser       users_idChat integer REFERENCES users (idChat),
    id_volunteer users_idChat integer REFERENCES users (idChat),
    start_date   integer,
    end_date     integer,
    status       integer
);








