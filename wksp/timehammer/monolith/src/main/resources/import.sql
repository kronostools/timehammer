INSERT INTO city(code, name, timezone)
VALUES ('MAD', 'Madrid', 'EUROPE_MADRID');

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

--INSERT INTO worker (internal_id, external_id, external_password, full_name)
--VALUES ('1', 'DM1', 'Dummy01', 'MOCKED DM1');

--INSERT INTO worker_preferences (worker_internal_id, worker_external_id, work_ssid,
--    work_start_mon, work_end_mon, lunch_start_mon, lunch_end_mon,
--    work_start_tue, work_end_tue, lunch_start_tue, lunch_end_tue,
--    work_start_wed, work_end_wed, lunch_start_wed, lunch_end_wed,
--    work_start_thu, work_end_thu, lunch_start_thu, lunch_end_thu,
--    work_start_fri, work_end_fri, lunch_start_fri, lunch_end_fri,
--    work_city_code)
--VALUES ('1', 'DM1', 'WIFI',
--    time '07:30', time '17:00', time '13:00', time '14:00',
--    time '07:30', time '17:00', time '13:00', time '14:00',
--    time '07:30', time '17:00', time '13:00', time '14:00',
--    time '07:30', time '17:00', time '13:00', time '14:00',
--    time '07:30', time '14:30', null, null,
--    'MAD');

--INSERT INTO worker_chat (worker_internal_id, chat_id)
--VALUES ('DM1', 1);

INSERT INTO worker_ssid_tracking_info (worker_internal_id, ssid_reported, reported)
SELECT internal_id, 'NA', TO_TIMESTAMP('1900-01-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS')
    FROM worker;