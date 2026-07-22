CREATE TABLE IF NOT EXISTS price_units
(
    id         bigserial PRIMARY KEY,

    -- Stable business code used internally by the application.
    -- Examples:
    -- PHR
    -- PNT
    -- PDY
    -- PPS
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

CREATE TABLE IF NOT EXISTS price_unit_locales
(
    id                 bigserial PRIMARY KEY,

    -- Parent price unit.
    price_unit_id      bigint                                             NOT NULL REFERENCES price_units (id) ON DELETE CASCADE,
    -- Translation language.
    locale_id          bigint                                             NOT NULL REFERENCES locales (id) ON DELETE CASCADE,

    -- Localized display name.
    -- Example:
    -- "Per Hour"
    name               varchar(100)                                       NOT NULL,
    -- Short explanation displayed in the UI.
    -- Example:
    -- "The customer is charged for every hour booked."
    description        text                                               NOT NULL DEFAULT '',
    -- Display order.
    sort_order         integer                  DEFAULT 0                 NOT NULL,

    -- Explains how the billing quantity is calculated.
    -- Example:
    -- "Total amount = booked hours × unit price."
    calculation_method text                                               NOT NULL DEFAULT '',
    -- Example shown to administrators.
    -- Example:
    -- "$30 × 3 hours = $90"
    usage_example      text                                               NOT NULL DEFAULT '',

    created_by         bigint                                             NOT NULL REFERENCES users (id),
    created_at         timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by         bigint                                             NOT NULL REFERENCES users (id),
    updated_at         timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version            bigint                   DEFAULT 0                 NOT NULL,
    is_active          boolean                  DEFAULT true              NOT NULL,
    is_deleted         boolean                  DEFAULT false             NOT NULL,
    deleted_by         bigint REFERENCES users (id),
    deleted_at         timestamp with time zone,

    -- A price unit can have only one translation per locale.
    CONSTRAINT uq_price_unit_locale
        UNIQUE (price_unit_id, locale_id)
);

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
        INSERT INTO price_unit_locales (price_unit_id, locale_id, name, description, sort_order, calculation_method,
                                        usage_example, created_by, updated_by)
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
                               '$120 × 5 nights = $600'),
                              ('PDY', 'en',
                               'Per Day',
                               'The customer is charged for each calendar day.',
                               'Total amount = number of days × unit price.',
                               '$80 × 3 days = $240'),
                              ('PPS', 'en',
                               'Per Person',
                               'The customer is charged for each person included in the booking.',
                               'Total amount = number of persons × unit price.',
                               '$50 × 4 persons = $200'))
                          v(code, locale_code, name, description, calculation_method, usage_example)
                      ON pu.code = v.code
                 JOIN locales l ON l.code = v.locale_code
        ON CONFLICT (price_unit_id, locale_id) DO NOTHING;

    END
$$;