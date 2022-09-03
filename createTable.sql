--Создание таблицы user "Пользователи"
CREATE TABLE users
(
    id       bigint PRIMARY KEY,
    idChat   bigint      NOT NULL,
    nameUser varchar(36) NOT NULL,
    menu     varchar,
    role     integer
);
--Создание таблицы pets "Питомцы"
CREATE TABLE pets
(
    id      integer PRIMARY KEY,
    type    integer     NOT NULL,
    name    varchar(36) NOT NULL,
    age     integer,
    comment varchar(36)
);
--Создание таблицы keepingPets "Ведение питомца"
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
--Создание таблицы directory "Справочник"
CREATE TABLE directory
(
    id   integer PRIMARY KEY,
    name text
);

--Создание таблицы user "Пользователи"
CREATE TABLE usersmenu
(
    id       BIGINT PRIMARY KEY,
    nameUser varchar(36) NOT NULL,
    idMenu   varchar(36),
    role     varchar(36)
);




