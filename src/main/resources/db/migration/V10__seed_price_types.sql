DO
$$
    DECLARE
        sys_id bigint;
    BEGIN
        SELECT id INTO sys_id FROM users WHERE username = 'system';

        -- =============================================
        -- 1. Price Types
        -- =============================================
        INSERT INTO price_types (code, sort_order, created_by, updated_by)
        VALUES ('BAS', 1, sys_id, sys_id),
               ('WKD', 2, sys_id, sys_id),
               ('WKE', 3, sys_id, sys_id),
               ('HOL', 4, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        -- =============================================
        -- 2. Price Type Locales (English)
        -- =============================================
        INSERT INTO price_type_locales (price_type_id, locale_id, name, description, sort_order, purpose,
                                        usage_example, created_by, updated_by)
        SELECT pt.id, l.id, v.name, v.description, pt.sort_order, v.purpose, v.usage_example, sys_id, sys_id
        FROM price_types pt
                 JOIN (VALUES ('BAS', 'en',
                               'Base Price',
                               'Standard rack rate applied by default when no other pricing rule is active.',
                               'Serves as the fallback price for all room bookings.',
                               'A Deluxe Room is listed at $100/night. With no active pricing rule in effect, the BAS rate of $100 applies.'),
                              ('WKD', 'en',
                               'Weekday Price',
                               'Rate applied on Monday through Friday.',
                               'Encourages mid-week bookings by offering competitive weekday pricing.',
                               'A Standard Room costs $80/night on weekdays (Mon–Fri) versus $120/night on weekends.'),
                              ('WKE', 'en',
                               'Weekend Price',
                               'Premium rate applied on Saturdays and Sundays.',
                               'Captures higher demand during weekend stays.',
                               'A Deluxe Room priced at $100/night on weekdays rises to $150/night under the WKE rate.'),
                              ('HOL', 'en',
                               'Holiday Price',
                               'Premium rate applied on public holidays and peak festive periods.',
                               'Maximises revenue during high-demand holiday seasons.',
                               'During the New Year period, a Suite is priced at $250/night under the HOL rate instead of the regular $180/night.'))
                          v(code, locale_code, name, description, purpose, usage_example)
                      ON pt.code = v.code
                 JOIN locales l ON l.code = v.locale_code
        ON CONFLICT (price_type_id, locale_id) DO NOTHING;

    END
$$;
