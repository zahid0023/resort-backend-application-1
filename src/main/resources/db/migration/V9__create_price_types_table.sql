CREATE TABLE IF NOT EXISTS price_types
(
    id         bigserial PRIMARY KEY,

    -- Stable business code used internally by the application.
    -- Examples:
    -- BAS
    -- WKD
    -- WKE
    -- HOL
    code       varchar(50)                                        NOT NULL UNIQUE,
    -- Display order in administrative interfaces.
    sort_order integer                  DEFAULT 0                 NOT NULL,

    created_by bigint                                             NOT NULL REFERENCES users (id),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by bigint                                             NOT NULL REFERENCES users (id),
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version    bigint                   DEFAULT 0                 NOT NULL,
    is_active  boolean                  DEFAULT true              NOT NULL,
    is_deleted boolean                  DEFAULT false             NOT NULL,
    deleted_by bigint REFERENCES users (id),
    deleted_at timestamp with time zone
);

CREATE TABLE IF NOT EXISTS price_type_locales
(
    id            bigserial PRIMARY KEY,

    -- Parent price type.
    price_type_id bigint                                             NOT NULL REFERENCES price_types (id) ON DELETE CASCADE,
    -- Translation language.
    locale_id     bigint                                             NOT NULL REFERENCES locales (id) ON DELETE CASCADE,

    -- Localized display name.
    -- Example:
    -- "Weekend Price"
    name          varchar(100)                                       NOT NULL,
    -- Short explanation shown in UI.
    -- Example:
    -- "Price applied to bookings made on Saturdays and Sundays."
    description   text                                               NOT NULL DEFAULT '',
    -- Business purpose of this price type.
    -- Example:
    -- "Allows higher pricing during weekends."
    -- Display order.
    sort_order    integer                  DEFAULT 0                 NOT NULL,

    purpose       text                                               NOT NULL DEFAULT '',
    -- Example scenario shown to administrators.
    -- Example:
    -- "Football Ground A costs $50/hour on weekdays and $70/hour on weekends."
    usage_example text                                               NOT NULL DEFAULT '',

    created_by    bigint                                             NOT NULL REFERENCES users (id),
    created_at    timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by    bigint                                             NOT NULL REFERENCES users (id),
    updated_at    timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version       bigint                   DEFAULT 0                 NOT NULL,
    is_active     boolean                  DEFAULT true              NOT NULL,
    is_deleted    boolean                  DEFAULT false             NOT NULL,
    deleted_by    bigint REFERENCES users (id),
    deleted_at    timestamp with time zone,

    CONSTRAINT uq_price_type_locale
        UNIQUE (price_type_id, locale_id)
);

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
                               'Standard price applied by default when no other price type matches.',
                               'Serves as the fallback pricing rule for all bookings.',
                               'A standard room costs $100/night under the base price.'),
                              ('WKD', 'en',
                               'Weekday Price',
                               'Price applied to bookings made on weekdays (Monday through Friday).',
                               'Allows lower pricing during off-peak weekday periods.',
                               'A room costs $90/night on weekdays compared to $130/night on weekends.'),
                              ('WKE', 'en',
                               'Weekend Price',
                               'Price applied to bookings made on Saturdays and Sundays.',
                               'Allows higher pricing during peak weekend demand.',
                               'Football Ground A costs $50/hour on weekdays and $70/hour on weekends.'),
                              ('HOL', 'en',
                               'Holiday Price',
                               'Price applied to bookings on public holidays or designated holiday periods.',
                               'Enables premium pricing during high-demand holiday seasons.',
                               'A deluxe room costs $200/night during the Christmas holiday period.'))
                          v(code, locale_code, name, description, purpose, usage_example)
                      ON pt.code = v.code
                 JOIN locales l ON l.code = v.locale_code
        ON CONFLICT (price_type_id, locale_id) DO NOTHING;

    END
$$;