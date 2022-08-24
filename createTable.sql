--Создание таблицы user "Пользователи"
CREATE TABLE users
(
    id       integer PRIMARY KEY,
    idChat   integer     NOT NULL,
    nameUser varchar(36) NOT NULL,
    phone    integer,
    idMenu   integer
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
CREATE TABLE keepingPets
(
    id        integer PRIMARY KEY,
    idPets    integer NOT NULL,
    idUsers   integer NOT NULL,
    foto      varchar,
    diet      varchar(36),
    wellBeing varchar,
    behavior  varchar
);
--Создание таблицы parentPets "Усыновитель питомца"
CREATE TABLE parentPets
(
    id        integer PRIMARY KEY,
    idPets    integer NOT NULL,
    idUsers   integer NOT NULL,
    fio      varchar,
    mail      varchar(36)
);
--Создание таблицы directory "Справочник"
CREATE TABLE directory
(
    id        integer PRIMARY KEY,
    name      text
);
--Создание таблицы volunteers "Волонтеры"
CREATE TABLE volunteers
(
    id        integer PRIMARY KEY,
    fio      varchar
);