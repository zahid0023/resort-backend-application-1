DO
$$
    DECLARE
        sys_id bigint;
    BEGIN
        SELECT id INTO sys_id FROM users WHERE username = 'system';

        -- =============================================
        -- 1. Price Units
        -- =============================================
        INSERT INTO price_units (code, sort_order, created_by, updated_by)
        VALUES ('PHR', 1, sys_id, sys_id),
               ('PNT', 2, sys_id, sys_id),
               ('PDY', 3, sys_id, sys_id),
               ('PPS', 4, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        -- =============================================
        -- 2. Price Unit Locales (English)
        -- =============================================
        INSERT INTO price_unit_locales (price_unit_id, locale_id, name, description, sort_order,
                                        calculation_method, usage_example, created_by, updated_by)
        SELECT pu.id, l.id, v.name, v.description, pu.sort_order, v.calculation_method, v.usage_example, sys_id,
               sys_id
        FROM price_units pu
                 JOIN (VALUES ('PHR', 'en',
                               'Per Hour',
                               'The customer is charged for every hour booked.',
                               'Total amount = booked hours × unit price.',
                               '$30 × 3 hours = $90'),
                              ('PNT', 'en',
                               'Per Night',
                               'The customer is charged for each night of the stay.',
                               'Total amount = number of nights × unit price.',
                               '$120 × 2 nights = $240'),
                              ('PDY', 'en',
                               'Per Day',
                               'The customer is charged for each calendar day of the booking.',
                               'Total amount = number of days × unit price.',
                               '$100 × 3 days = $300'),
                              ('PPS', 'en',
                               'Per Person',
                               'The customer is charged based on the number of guests in the booking.',
                               'Total amount = number of persons × unit price.',
                               '$50 × 4 persons = $200'))
                          v(code, locale_code, name, description, calculation_method, usage_example)
                      ON pu.code = v.code
                 JOIN locales l ON l.code = v.locale_code
        ON CONFLICT (price_unit_id, locale_id) DO NOTHING;

    END
$$;
