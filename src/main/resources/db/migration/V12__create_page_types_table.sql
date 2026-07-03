CREATE TABLE IF NOT EXISTS page_types
(
    -- Primary key
    id         BIGSERIAL PRIMARY KEY,

    -- Stable system code (never translated)
    code       VARCHAR(100)             NOT NULL UNIQUE,
    -- Display order in UI
    sort_order INTEGER                  NOT NULL DEFAULT 1,

    -- Audit fields
    created_by BIGINT                   NOT NULL REFERENCES users (id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT                   NOT NULL REFERENCES users (id),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version    BIGINT                   NOT NULL DEFAULT 0,
    -- Soft delete
    is_active  BOOLEAN                  NOT NULL DEFAULT TRUE,
    is_deleted BOOLEAN                  NOT NULL DEFAULT FALSE,
    deleted_by BIGINT REFERENCES users (id),
    deleted_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS page_type_locales
(
    -- Primary key
    id           BIGSERIAL PRIMARY KEY,

    -- Parent page type
    page_type_id BIGINT                   NOT NULL
        REFERENCES page_types (id),
    -- Language
    locale_id    BIGINT                   NOT NULL
        REFERENCES locales (id),

    -- Localized values
    name         VARCHAR(255)             NOT NULL,
    description  TEXT                              DEFAULT '',
    -- Display order (can vary by locale if needed)
    sort_order   INTEGER                  NOT NULL DEFAULT 1,

    -- Audit fields
    created_by   BIGINT                   NOT NULL REFERENCES users (id),
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by   BIGINT                   NOT NULL REFERENCES users (id),
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version      BIGINT                   NOT NULL DEFAULT 0,

    -- Soft delete
    is_active    BOOLEAN                  NOT NULL DEFAULT TRUE,
    is_deleted   BOOLEAN                  NOT NULL DEFAULT FALSE,
    deleted_by   BIGINT REFERENCES users (id),
    deleted_at   TIMESTAMP WITH TIME ZONE,

    -- One translation per locale
    CONSTRAINT uk_page_type_locale
        UNIQUE (page_type_id, locale_id)
);

DO
$$
    DECLARE
        sys_id bigint;
    BEGIN
        SELECT id INTO sys_id FROM users WHERE username = 'system';

        -- =============================================
        -- 1. Page Types
        -- =============================================
        INSERT INTO page_types (code, sort_order, created_by, updated_by)
        VALUES ('HOM', 1, sys_id, sys_id),
               ('RMS', 2, sys_id, sys_id),
               ('DNG', 3, sys_id, sys_id),
               ('FAC', 4, sys_id, sys_id),
               ('EVT', 5, sys_id, sys_id),
               ('GAL', 6, sys_id, sys_id),
               ('ABT', 7, sys_id, sys_id),
               ('CON', 8, sys_id, sys_id),
               ('LND', 9, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        -- =============================================
        -- 2. Page Type Locales (English)
        -- =============================================
        INSERT INTO page_type_locales (page_type_id, locale_id, name, description, sort_order, created_by, updated_by)
        SELECT pt.id, l.id, v.name, v.description, pt.sort_order, sys_id, sys_id
        FROM page_types pt
                 JOIN (VALUES ('HOM',
                               'Home Page',
                               'Main home page of the resort website.'),
                              ('RMS',
                               'Rooms Page',
                               'Showcases available room types, categories, and pricing.'),
                              ('DNG',
                               'Dining Page',
                               'Highlights restaurants, bars, and dining experiences at the resort.'),
                              ('FAC',
                               'Facilities Page',
                               'Presents resort amenities such as pools, spas, gyms, and recreational areas.'),
                              ('EVT',
                               'Events Page',
                               'Lists upcoming events, conferences, and special occasions hosted at the resort.'),
                              ('GAL',
                               'Gallery Page',
                               'Visual gallery showcasing resort photos, rooms, and facilities.'),
                              ('ABT',
                               'About Page',
                               'Provides information about the resort history, values, and team.'),
                              ('CON',
                               'Contact Page',
                               'Contact details, location map, and inquiry form for the resort.'),
                              ('LND',
                               'Landing Page',
                               'Dedicated promotional landing page for campaigns or seasonal offers.')) v(code, name, description)
                      ON pt.code = v.code
                 JOIN locales l ON l.code = 'en'
        ON CONFLICT (page_type_id, locale_id) DO NOTHING;

    END
$$;
