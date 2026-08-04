CREATE TABLE IF NOT EXISTS resort_permission_types
(
    id         bigserial PRIMARY KEY,

    code       varchar(150)             NOT NULL,
    sort_order integer                  NOT NULL DEFAULT 1,

    created_by bigint                   NOT NULL REFERENCES users (id),
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint                   NOT NULL REFERENCES users (id),
    updated_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version    bigint                   NOT NULL DEFAULT 0,
    is_active  boolean                  NOT NULL DEFAULT true,
    is_deleted boolean                  NOT NULL DEFAULT false,
    deleted_by bigint REFERENCES users (id),
    deleted_at timestamp with time zone,

    CONSTRAINT uq_resort_permission_types_code
        UNIQUE (code),

    CONSTRAINT chk_resort_permission_types_sort_order
        CHECK (sort_order >= 0)
);

CREATE INDEX idx_resort_permission_types_sort_order
    ON resort_permission_types (sort_order);

CREATE TABLE IF NOT EXISTS resort_permission_type_locales
(
    id                        bigserial PRIMARY KEY,

    resort_permission_type_id bigint                   NOT NULL
        REFERENCES resort_permission_types (id),
    locale_id                 bigint                   NOT NULL
        REFERENCES locales (id),

    name                      varchar(255)             NOT NULL,
    description               text                     NOT NULL DEFAULT '',
    sort_order                integer                  NOT NULL DEFAULT 1,

    created_by                bigint                   NOT NULL REFERENCES users (id),
    created_at                timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by                bigint                   NOT NULL REFERENCES users (id),
    updated_at                timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version                   bigint                   NOT NULL DEFAULT 0,
    is_active                 boolean                  NOT NULL DEFAULT true,
    is_deleted                boolean                  NOT NULL DEFAULT false,
    deleted_by                bigint REFERENCES users (id),
    deleted_at                timestamp with time zone,

    CONSTRAINT uq_resort_permission_type_locale
        UNIQUE (resort_permission_type_id, locale_id)
);

CREATE INDEX idx_resort_permission_type_locales_permission
    ON resort_permission_type_locales (resort_permission_type_id);

CREATE INDEX idx_resort_permission_type_locales_locale
    ON resort_permission_type_locales (locale_id);

-- ============================================================
-- Seed data
-- ============================================================

DO
$$
    DECLARE
        sys_id bigint;
    BEGIN
        SELECT id INTO sys_id FROM users WHERE username = 'system';

        INSERT INTO resort_permission_types (code, sort_order, created_by, updated_by)
        VALUES ('ALL_PERMISSIONS', 0, sys_id, sys_id),
               ('VIEW_BOOKING',   1, sys_id, sys_id),
               ('CREATE_BOOKING', 2, sys_id, sys_id),
               ('CANCEL_BOOKING', 3, sys_id, sys_id),
               ('MANAGE_ROOMS',   4, sys_id, sys_id),
               ('VIEW_REPORTS',   5, sys_id, sys_id),
               ('MANAGE_STAFF',   6, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        INSERT INTO resort_permission_type_locales (resort_permission_type_id, locale_id, name, description,
                                                    sort_order, created_by, updated_by)
        SELECT p.id, l.id, v.name, v.description, p.sort_order, sys_id, sys_id
        FROM resort_permission_types p
                 JOIN (VALUES ('ALL_PERMISSIONS',
                               'All Permissions',
                               'Grants full access to all resort operations.'),
                              ('VIEW_BOOKING',
                               'View Bookings',
                               'Can view all reservations and booking details for the resort.'),
                              ('CREATE_BOOKING',
                               'Create Bookings',
                               'Can create new reservations on behalf of guests.'),
                              ('CANCEL_BOOKING',
                               'Cancel Bookings',
                               'Can cancel existing reservations.'),
                              ('MANAGE_ROOMS',
                               'Manage Rooms',
                               'Can manage room availability, pricing, and configuration.'),
                              ('VIEW_REPORTS',
                               'View Reports',
                               'Can access occupancy, revenue, and operational reports.'),
                              ('MANAGE_STAFF',
                               'Manage Staff',
                               'Can assign and revoke resort access for other users.'))
                          v(code, name, description)
                      ON p.code = v.code
                 JOIN locales l ON l.code = 'en'
        ON CONFLICT (resort_permission_type_id, locale_id) DO NOTHING;
    END
$$;
