DELETE FROM worker_holiday;
DELETE FROM worker_preferences;
DELETE FROM worker;

INSERT INTO worker (internal_id, full_name, profile)
VALUES ('1111-1111-1111-1111', 'CASTRO VIADERO DIEGO', 'ADMIN'),
       ('2222-2222-2222-2222', 'FERNANDEZ GONZALEZ ALVARO', 'WORKER');

INSERT INTO worker_holiday (worker_internal_id, day)
VALUES ('1111-1111-1111-1111', CAST('2020-01-01' AS date)),
       ('1111-1111-1111-1111', CAST('2020-02-01' AS date)),
       ('1111-1111-1111-1111', CAST('2020-03-01' AS date)),
       ('1111-1111-1111-1111', CAST('2020-04-01' AS date)),
       ('1111-1111-1111-1111', CAST('2020-05-01' AS date)),
       ('1111-1111-1111-1111', CAST('2020-06-01' AS date)),
       ('1111-1111-1111-1111', CAST('2020-07-01' AS date)),
       ('1111-1111-1111-1111', CAST('2020-08-01' AS date)),
       ('1111-1111-1111-1111', CAST('2020-09-01' AS date)),
       ('1111-1111-1111-1111', CAST('2020-10-01' AS date)),
       ('1111-1111-1111-1111', CAST('2020-11-01' AS date)),
       ('1111-1111-1111-1111', CAST('2020-12-01' AS date)),
       ('1111-1111-1111-1111', CAST('2020-01-05' AS date)),
       ('1111-1111-1111-1111', CAST('2020-02-05' AS date)),
       ('1111-1111-1111-1111', CAST('2020-03-05' AS date)),
       ('1111-1111-1111-1111', CAST('2020-04-05' AS date)),
       ('1111-1111-1111-1111', CAST('2020-05-05' AS date)),
       ('1111-1111-1111-1111', CAST('2020-06-05' AS date)),
       ('1111-1111-1111-1111', CAST('2020-07-05' AS date)),
       ('1111-1111-1111-1111', CAST('2020-08-05' AS date)),
       ('1111-1111-1111-1111', CAST('2020-09-05' AS date)),
       ('1111-1111-1111-1111', CAST('2020-10-05' AS date)),
       ('1111-1111-1111-1111', CAST('2020-11-05' AS date)),
       ('1111-1111-1111-1111', CAST('2020-12-05' AS date)),
       ('1111-1111-1111-1111', CAST('2020-01-10' AS date)),
       ('1111-1111-1111-1111', CAST('2020-02-10' AS date));

INSERT INTO worker_preferences (worker_internal_id, worker_external_id,
    work_start_mon, work_end_mon, lunch_start_mon, lunch_end_mon,
    work_start_tue, work_end_tue, lunch_start_tue, lunch_end_tue,
    work_start_wed, work_end_wed, lunch_start_wed, lunch_end_wed,
    work_start_thu, work_end_thu, lunch_start_thu, lunch_end_thu,
    work_start_fri, work_end_fri, lunch_start_fri, lunch_end_fri,
    company_code, work_ssid, work_city_code)
VALUES
    ('1111-1111-1111-1111', 'DCV',
     CAST('07:40:00' AS time), CAST('07:40:00' AS time), CAST('13:10:00' AS time), CAST('14:10:00' AS time),
     CAST('07:40:00' AS time), CAST('07:40:00' AS time), CAST('13:10:00' AS time), CAST('14:10:00' AS time),
     CAST('07:40:00' AS time), CAST('07:40:00' AS time), CAST('13:10:00' AS time), CAST('14:10:00' AS time),
     CAST('07:40:00' AS time), CAST('07:40:00' AS time), CAST('13:10:00' AS time), CAST('14:10:00' AS time),
     CAST('07:40:00' AS time), CAST('07:40:00' AS time), null, null,
     'COMUNYTEK', 'BBVA', 'MAD'),
     ('2222-2222-2222-2222', 'AFG',
      CAST('07:40:00' AS time), CAST('07:40:00' AS time), CAST('13:10:00' AS time), CAST('14:10:00' AS time),
      CAST('07:40:00' AS time), CAST('07:40:00' AS time), CAST('13:10:00' AS time), CAST('14:10:00' AS time),
      CAST('07:40:00' AS time), CAST('07:40:00' AS time), CAST('13:10:00' AS time), CAST('14:10:00' AS time),
      CAST('07:40:00' AS time), CAST('07:40:00' AS time), CAST('13:10:00' AS time), CAST('14:10:00' AS time),
      CAST('07:40:00' AS time), CAST('07:40:00' AS time), null, null,
      'COMUNYTEK', 'BBVA', 'MAD');