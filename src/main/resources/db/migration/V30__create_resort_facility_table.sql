CREATE TABLE IF NOT EXISTS resort_facilities
(
    id                       bigserial PRIMARY KEY,

    -- Resort
    resort_id                bigint                   NOT NULL REFERENCES resorts (id),

    -- Resort facility group this facility belongs to.
    resort_facility_group_id bigint                   NOT NULL REFERENCES resort_facility_groups (id),

    -- Optional platform facility.
    -- NULL means this is a custom facility created by the resort.
    facility_id              bigint REFERENCES facilities (id),

    sort_order               integer                  NOT NULL DEFAULT 1,

    -- Optional visual override.
    -- If NULL and facility_id is not NULL,
    -- use the platform facility icon.
    icon_type                varchar(100),
    icon_value               text,
    icon_meta                jsonb,

    created_by               bigint                   NOT NULL REFERENCES users (id),
    created_at               timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by               bigint                   NOT NULL REFERENCES users (id),
    updated_at               timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version                  bigint                   NOT NULL DEFAULT 0,
    is_active                boolean                  NOT NULL DEFAULT true,
    is_deleted               boolean                  NOT NULL DEFAULT false,
    deleted_by               bigint REFERENCES users (id),
    deleted_at               timestamp with time zone
);

CREATE INDEX IF NOT EXISTS idx_resort_facilities_resort
    ON resort_facilities (resort_id);

CREATE INDEX IF NOT EXISTS idx_resort_facilities_group
    ON resort_facilities (resort_facility_group_id);

CREATE INDEX IF NOT EXISTS idx_resort_facilities_platform
    ON resort_facilities (facility_id);

CREATE TABLE IF NOT EXISTS resort_facility_locales
(
    id                 bigserial PRIMARY KEY,

    resort_facility_id bigint                   NOT NULL
        REFERENCES resort_facilities (id)
            ON DELETE CASCADE,

    locale_id          bigint                   NOT NULL
        REFERENCES locales (id),

    -- If linked to a platform facility,
    -- these override the platform values.
    -- If facility_id is NULL,
    -- these are the primary values.
    name               varchar(255)             NOT NULL,
    description        text                     NOT NULL DEFAULT '',
    sort_order         integer                  NOT NULL DEFAULT 1,

    created_by         bigint                   NOT NULL REFERENCES users (id),
    created_at         timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by         bigint                   NOT NULL REFERENCES users (id),
    updated_at         timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version            bigint                   NOT NULL DEFAULT 0,
    is_active          boolean                  NOT NULL DEFAULT true,
    is_deleted         boolean                  NOT NULL DEFAULT false,
    deleted_by         bigint REFERENCES users (id),
    deleted_at         timestamp with time zone,

    CONSTRAINT uk_resort_facility_locale
        UNIQUE (resort_facility_id, locale_id)
);

CREATE INDEX IF NOT EXISTS idx_resort_facility_locales_locale
    ON resort_facility_locales (locale_id);