CREATE TABLE IF NOT EXISTS resort_facility_groups
(
    id                bigserial PRIMARY KEY,

    -- Resort that owns this facility group.
    resort_id         bigint                   NOT NULL REFERENCES resorts (id),

    -- Optional reference to a platform facility group.
    -- NULL means this is a resort-defined custom group.
    facility_group_id bigint REFERENCES facility_groups (id),

    -- Resort-specific presentation.
    sort_order        integer                  NOT NULL DEFAULT 1,

    -- Optional override for the platform icon.
    -- For custom groups, these become the primary icon fields.
    icon_type         varchar(100),
    icon_value        text,
    icon_meta         jsonb,

    created_by        bigint                   NOT NULL REFERENCES users (id),
    created_at        timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        bigint                   NOT NULL REFERENCES users (id),
    updated_at        timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version           bigint                   NOT NULL DEFAULT 0,
    is_active         boolean                  NOT NULL DEFAULT true,
    is_deleted        boolean                  NOT NULL DEFAULT false,
    deleted_by        bigint REFERENCES users (id),
    deleted_at        timestamp with time zone
);

CREATE INDEX IF NOT EXISTS idx_resort_facility_groups_resort
    ON resort_facility_groups (resort_id);

CREATE INDEX IF NOT EXISTS idx_resort_facility_groups_platform
    ON resort_facility_groups (facility_group_id);

-- One resort can map to the same platform facility group at most once.
-- Custom groups (facility_group_id IS NULL) are excluded from this constraint.
CREATE UNIQUE INDEX IF NOT EXISTS uk_resort_facility_groups_resort_platform
    ON resort_facility_groups (resort_id, facility_group_id)
    WHERE facility_group_id IS NOT NULL;

CREATE TABLE IF NOT EXISTS resort_facility_group_locales
(
    id                       bigserial PRIMARY KEY,

    resort_facility_group_id bigint                   NOT NULL
        REFERENCES resort_facility_groups (id)
            ON DELETE CASCADE,

    locale_id                bigint                   NOT NULL
        REFERENCES locales (id),

    -- If linked to a platform group, these override the defaults.
    -- If this is a custom group, these are the actual values.
    name                     varchar(255)             NOT NULL,
    description              text                     NOT NULL DEFAULT '',
    -- Resort-specific presentation.
    sort_order               integer                  NOT NULL DEFAULT 1,

    created_by               bigint                   NOT NULL REFERENCES users (id),
    created_at               timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by               bigint                   NOT NULL REFERENCES users (id),
    updated_at               timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version                  bigint                   NOT NULL DEFAULT 0,
    is_active                boolean                  NOT NULL DEFAULT true,
    is_deleted               boolean                  NOT NULL DEFAULT false,
    deleted_by               bigint REFERENCES users (id),
    deleted_at               timestamp with time zone,

    CONSTRAINT uk_resort_facility_group_locale
        UNIQUE (resort_facility_group_id, locale_id)
);

CREATE INDEX IF NOT EXISTS idx_resort_facility_group_locales_locale
    ON resort_facility_group_locales (locale_id);