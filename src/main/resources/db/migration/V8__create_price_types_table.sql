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