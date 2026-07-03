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