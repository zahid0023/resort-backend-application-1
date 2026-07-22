DO
$$
    DECLARE
        sys_id bigint;
    BEGIN
        SELECT id INTO sys_id FROM users WHERE username = 'system';

        -- =============================================
        -- 1. Special Price Type
        -- =============================================
        INSERT INTO price_types (code, sort_order, created_by, updated_by)
        VALUES ('SPE', 5, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        -- =============================================
        -- 2. Special Price Type Locale (English)
        -- =============================================
        INSERT INTO price_type_locales (price_type_id, locale_id, name, description, sort_order, purpose,
                                        usage_example, created_by, updated_by)
        SELECT pt.id, l.id, v.name, v.description, pt.sort_order, v.purpose, v.usage_example, sys_id, sys_id
        FROM price_types pt
                 JOIN (VALUES ('SPE', 'en',
                               'Special Price',
                               'Custom promotional or event-specific price applied for a selected date range.',
                               'Enables flexible pricing for promotions, festivals, events, or seasonal discounts.',
                               'A Summer Sale offers rooms at $75/night from June 1 – August 31, overriding all other active pricing rules.'))
                          v(code, locale_code, name, description, purpose, usage_example)
                      ON pt.code = v.code
                 JOIN locales l ON l.code = v.locale_code
        ON CONFLICT (price_type_id, locale_id) DO NOTHING;

    END
$$;
