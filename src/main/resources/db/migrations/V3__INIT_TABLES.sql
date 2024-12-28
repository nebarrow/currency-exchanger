INSERT INTO currencies (code, name, sign)
VALUES ('USD', 'US Dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('RUB', 'Russian Ruble', '₽'),
       ('AUD', 'Australian Dollar', 'A$');

INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
SELECT
    (SELECT id FROM currencies WHERE code='USD') AS base_currency_id,
    (SELECT id FROM currencies WHERE code='EUR') AS target_currency_id,
    0.95
UNION ALL
SELECT
    (SELECT id FROM currencies WHERE code='EUR') AS base_currency_id,
    (SELECT id FROM currencies WHERE code='RUB') AS target_currency_id,
    106.26;
