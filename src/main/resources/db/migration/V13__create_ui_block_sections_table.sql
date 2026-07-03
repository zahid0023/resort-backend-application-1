CREATE TABLE IF NOT EXISTS ui_block_sections
(
    -- Primary key
    id         BIGSERIAL PRIMARY KEY,

    -- Stable system code
    code       VARCHAR(100)             NOT NULL UNIQUE,
    -- Display order
    sort_order INTEGER                  NOT NULL DEFAULT 1,

    -- Audit
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

CREATE TABLE IF NOT EXISTS ui_block_section_locales
(
    -- Primary key
    id                  BIGSERIAL PRIMARY KEY,

    -- Parent Section
    ui_block_section_id BIGINT                   NOT NULL
        REFERENCES ui_block_sections (id),
    -- Locale
    locale_id           BIGINT                   NOT NULL
        REFERENCES locales (id),

    -- Localized values
    name                VARCHAR(255)             NOT NULL,
    description         TEXT                              DEFAULT '',
    sort_order          INTEGER                  NOT NULL DEFAULT 1,

    -- Audit
    created_by          BIGINT                   NOT NULL REFERENCES users (id),
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by          BIGINT                   NOT NULL REFERENCES users (id),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version             BIGINT                   NOT NULL DEFAULT 0,

    -- Soft delete
    is_active           BOOLEAN                  NOT NULL DEFAULT TRUE,
    is_deleted          BOOLEAN                  NOT NULL DEFAULT FALSE,
    deleted_by          BIGINT REFERENCES users (id),
    deleted_at          TIMESTAMP WITH TIME ZONE,

    CONSTRAINT uk_ui_block_section_locale
        UNIQUE (ui_block_section_id, locale_id)
);

DO
$$
    DECLARE
        sys_id bigint;
    BEGIN
        SELECT id INTO sys_id FROM users WHERE username = 'system';

        -- =============================================
        -- 1. UI Block Sections
        -- =============================================
        INSERT INTO ui_block_sections (code, sort_order, created_by, updated_by)
        VALUES ('HRO', 1,  sys_id, sys_id),
               ('HDR', 2,  sys_id, sys_id),
               ('CNT', 3,  sys_id, sys_id),
               ('FEA', 4,  sys_id, sys_id),
               ('GAL', 5,  sys_id, sys_id),
               ('TST', 6,  sys_id, sys_id),
               ('PRM', 7,  sys_id, sys_id),
               ('CTA', 8,  sys_id, sys_id),
               ('SDB', 9,  sys_id, sys_id),
               ('FTR', 10, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        -- =============================================
        -- 2. UI Block Section Locales (English)
        -- =============================================
        INSERT INTO ui_block_section_locales (ui_block_section_id, locale_id, name, description, sort_order,
                                              created_by, updated_by)
        SELECT s.id, l.id, v.name, v.description, s.sort_order, sys_id, sys_id
        FROM ui_block_sections s
                 JOIN (VALUES ('HRO',
                               'Hero',
                               'Full-width hero banner displayed at the top of a page.'),
                              ('HDR',
                               'Header',
                               'Page header area containing navigation and branding elements.'),
                              ('CNT',
                               'Content',
                               'Main body content section for text, media, and rich page content.'),
                              ('FEA',
                               'Features',
                               'Highlights key resort amenities, room features, or service offerings.'),
                              ('GAL',
                               'Gallery',
                               'Visual image or video gallery showcasing resort spaces and experiences.'),
                              ('TST',
                               'Testimonials',
                               'Guest reviews and testimonials to build trust and social proof.'),
                              ('PRM',
                               'Promotional Banner',
                               'Seasonal offers, campaigns, or special deal banners.'),
                              ('CTA',
                               'Call to Action',
                               'Encourages the visitor to take a specific action such as booking or enquiring.'),
                              ('SDB',
                               'Sidebar',
                               'Secondary side panel for supplementary content, filters, or quick links.'),
                              ('FTR',
                               'Footer',
                               'Bottom-of-page section containing links, contacts, and legal information.'))
                          v(code, name, description)
                      ON s.code = v.code
                 JOIN locales l ON l.code = 'en'
        ON CONFLICT (ui_block_section_id, locale_id) DO NOTHING;

    END
$$;