-- ============================================================
-- Resort Access Types
-- ============================================================

CREATE TABLE IF NOT EXISTS resort_access_types
(
    id         BIGSERIAL PRIMARY KEY,

    code       VARCHAR(100)             NOT NULL UNIQUE,
    sort_order INTEGER                  NOT NULL DEFAULT 1,

    created_by BIGINT                   NOT NULL REFERENCES users (id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT                   NOT NULL REFERENCES users (id),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version    BIGINT                   NOT NULL DEFAULT 0,
    is_active  BOOLEAN                  NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN                  NOT NULL DEFAULT FALSE,
    deleted_by BIGINT REFERENCES users (id),
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS resort_access_type_locales
(
    id                    BIGSERIAL PRIMARY KEY,

    resort_access_type_id BIGINT                   NOT NULL REFERENCES resort_access_types (id),
    locale_id             BIGINT                   NOT NULL REFERENCES locales (id),

    name                  VARCHAR(255)             NOT NULL,
    description           TEXT                              DEFAULT '',
    sort_order            INTEGER                  NOT NULL DEFAULT 1,

    created_by            BIGINT                   NOT NULL REFERENCES users (id),
    created_at            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by            BIGINT                   NOT NULL REFERENCES users (id),
    updated_at            TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version               BIGINT                   NOT NULL DEFAULT 0,
    is_active             BOOLEAN                  NOT NULL DEFAULT TRUE,
    is_deleted            BOOLEAN                  NOT NULL DEFAULT FALSE,
    deleted_by            BIGINT REFERENCES users (id),
    deleted_at            TIMESTAMP WITH TIME ZONE,

    CONSTRAINT uk_resort_access_type_locale UNIQUE (resort_access_type_id, locale_id)
);

-- ============================================================
-- Seed data
-- ============================================================

DO
$$
    DECLARE
        sys_id bigint;
    BEGIN
        SELECT id INTO sys_id FROM users WHERE username = 'system';

        INSERT INTO resort_access_types (code, sort_order, created_by, updated_by)
        VALUES ('OWNER',  1, sys_id, sys_id),
               ('BOOKER', 2, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        INSERT INTO resort_access_type_locales (resort_access_type_id, locale_id, name, description, sort_order,
                                                created_by, updated_by)
        SELECT a.id, l.id, v.name, v.description, a.sort_order, sys_id, sys_id
        FROM resort_access_types a
                 JOIN (VALUES ('OWNER',
                               'Owner',
                               'Full control over the resort — can manage settings, staff, and bookings.'),
                              ('BOOKER',
                               'Booker',
                               'Can browse resort details and make reservations on behalf of guests.'))
                          v(code, name, description)
                      ON a.code = v.code
                 JOIN locales l ON l.code = 'en'
        ON CONFLICT (resort_access_type_id, locale_id) DO NOTHING;
    END
$$;
