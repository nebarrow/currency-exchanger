INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
VALUES (
           (SELECT id FROM currencies WHERE code = 'AUD'),
           (SELECT id FROM currencies WHERE code = 'RUB'),
           63.93
       ),
       (
           (SELECT id FROM currencies WHERE code = 'USD'),
           (SELECT id FROM currencies WHERE code = 'RUB'),
           98.09
       );