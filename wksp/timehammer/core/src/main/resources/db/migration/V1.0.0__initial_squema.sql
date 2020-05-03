CREATE TABLE city (
    code varchar(255) not null,
    name varchar(255) not null,
    timezone varchar(255) not null,
    primary key (code)
);

CREATE TABLE city_holiday (
    city_code varchar(255) not null,
    day date not null,
    description varchar(255),
    primary key (city_code, day)
);

ALTER TABLE IF EXISTS city_holiday
    ADD CONSTRAINT fk_city_holiday__city
    FOREIGN KEY (city_code)
    REFERENCES city;

CREATE TABLE trash_message (
    chat_id varchar(255) not null,
    timestamp timestamp not null,
    status varchar(255) not null,
    text varchar(255) not null,
    primary key (chat_id, timestamp)
);

CREATE TABLE worker (
    internal_id varchar(255) not null,
    full_name varchar(255),
    profile varchar(255) not null,
    primary key (internal_id)
);

CREATE TABLE worker_chat (
    worker_internal_id varchar(255) not null,
    chat_id varchar(255) not null,
    primary key (worker_internal_id, chat_id)
);

ALTER TABLE IF EXISTS worker_chat
    ADD CONSTRAINT uk_worker_chat__chat_id unique (chat_id);

ALTER TABLE IF EXISTS worker_chat
    ADD CONSTRAINT fk_worker_chat__worker_external_id
    FOREIGN KEY (worker_internal_id)
    REFERENCES worker;

CREATE TABLE worker_holiday (
    worker_internal_id varchar(255) not null,
    day date not null,
    primary key (worker_internal_id, day)
);

ALTER TABLE IF EXISTS worker_holiday
    ADD CONSTRAINT fk_worker_holiday__worker_internal_id
    FOREIGN KEY (worker_internal_id)
    REFERENCES worker;

CREATE TABLE worker_preferences (
    worker_internal_id varchar(255) not null,
    worker_external_id varchar(255) not null,
    work_start_mon time not null,
    work_end_mon time not null,
    lunch_start_mon time not null,
    lunch_end_mon time not null,
    work_start_tue time not null,
    work_end_tue time not null,
    lunch_start_tue time not null,
    lunch_end_tue time not null,
    work_start_wed time not null,
    work_end_wed time not null,
    lunch_start_wed time not null,
    lunch_end_wed time not null,
    work_start_thu time not null,
    work_end_thu time not null,
    lunch_start_thu time not null,
    lunch_end_thu time not null,
    work_start_fri time not null,
    work_end_fri time not null,
    lunch_start_fri time,
    lunch_end_fri time,
    company_code varchar(255) not null,
    work_ssid varchar(255) not null,
    work_city_code varchar(255),
    primary key (worker_internal_id)
);

ALTER TABLE IF EXISTS worker_preferences
    ADD CONSTRAINT fk_worker_preferences__work_city_code
    FOREIGN KEY (work_city_code)
    REFERENCES city;

ALTER TABLE IF EXISTS worker_preferences
    ADD CONSTRAINT fk_worker_preferences__worker_external_id
    FOREIGN KEY (worker_internal_id)
    REFERENCES worker;

INSERT INTO city(code, name, timezone)
VALUES ('MAD', 'Madrid', 'Europe/Madrid');

INSERT INTO city_holiday(city_code, day, description)
VALUES ('MAD', '2020-01-01', 'año nuevo'),
       ('MAD', '2020-01-06', 'reyes'),
       ('MAD', '2020-04-09', 'jueves santo'),
       ('MAD', '2020-04-10', 'viernes santo'),
       ('MAD', '2020-05-01', 'primero de mayo'),
       ('MAD', '2020-05-02', 'fiesta de la comunidad de madrid'),
       ('MAD', '2020-05-15', 'san isidro'),
       ('MAD', '2020-08-15', 'asunción de la virgen'),
       ('MAD', '2020-10-12', 'día de la hispanidad'),
       ('MAD', '2020-11-02', 'todos los santos (trasladada)'),
       ('MAD', '2020-11-09', 'la almudena'),
       ('MAD', '2020-12-07', 'la constitución (trasladada)'),
       ('MAD', '2020-12-08', 'la inmaculada'),
       ('MAD', '2020-12-25', 'navidad');