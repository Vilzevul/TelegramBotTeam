--Создание таблицы user "Пользователи"
CREATE TABLE users
(
    id       bigint PRIMARY KEY,
    idChat   bigint      NOT NULL,
    nameUser varchar(36) NOT NULL,
    menu     varchar,
    role     integer
);

--Создание таблицы report "Ведение питомца"
CREATE TABLE report
(
    id        integer PRIMARY KEY,
    idPets    integer NOT NULL,
    idUsers   integer NOT NULL,
    foto      bytea,
    diet      varchar(36),
    wellBeing varchar,
    behavior  varchar
);






