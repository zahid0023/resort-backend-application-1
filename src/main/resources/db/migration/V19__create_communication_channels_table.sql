CREATE TABLE IF NOT EXISTS communication_channels
(
    id           bigserial PRIMARY KEY,

    -- Unique internal code used by the application.
    -- Examples:
    -- PHONE
    -- MOBILE
    -- EMAIL
    -- WHATSAPP
    -- FAX
    -- WEBSITE
    -- FACEBOOK
    -- INSTAGRAM
    -- X
    -- LINKEDIN
    -- TELEGRAM
    -- WECHAT
    code         varchar(50)                  NOT NULL UNIQUE,
    -- Display order in admin UI.
    sort_order   integer                      NOT NULL DEFAULT 0,

    -- Whether this channel expects a URL.
    -- Example: WEBSITE, FACEBOOK, INSTAGRAM.
    is_url       boolean                      NOT NULL DEFAULT FALSE,

    -- Whether this channel represents a phone number.
    -- Example: PHONE, MOBILE, WHATSAPP.
    is_phone     boolean                      NOT NULL DEFAULT FALSE,

    -- Whether this channel represents an email address.
    -- Example: EMAIL.
    is_email     boolean                      NOT NULL DEFAULT FALSE,

    -- Whether the value should be clickable in UI.
    is_clickable boolean                      NOT NULL DEFAULT TRUE,

    created_by   bigint references users (id) NOT NULL,
    created_at   timestamp with time zone     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by   bigint references users (id) NOT NULL,
    updated_at   timestamp with time zone     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version      bigint                                DEFAULT 0 NOT NULL,
    is_active    boolean                      NOT NULL DEFAULT true,
    is_deleted   boolean                      NOT NULL DEFAULT false,
    deleted_by   bigint,
    deleted_at   timestamp with time zone

);

CREATE TABLE IF NOT EXISTS communication_channel_locales
(
    id                       bigserial PRIMARY KEY,

    -- Parent communication channel.
    communication_channel_id bigint                       NOT NULL
        REFERENCES communication_channels (id),

    -- Language of this translation.
    locale_id                bigint                       NOT NULL
        REFERENCES locales (id),

    -- Localized display name.
    -- Examples:
    -- English : Phone
    -- Bangla  : ফোন
    name                     varchar(100)                 NOT NULL,
    -- Optional localized description.
    description              text,
    sort_order               integer                      NOT NULL DEFAULT 0,

    created_by               bigint references users (id) NOT NULL,
    created_at               timestamp with time zone     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by               bigint references users (id) NOT NULL,
    updated_at               timestamp with time zone     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version                  bigint                                DEFAULT 0 NOT NULL,
    is_active                boolean                      NOT NULL DEFAULT true,
    is_deleted               boolean                      NOT NULL DEFAULT false,
    deleted_by               bigint,
    deleted_at               timestamp with time zone,
    UNIQUE (communication_channel_id, locale_id)
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

        INSERT INTO communication_channels (code, sort_order, is_phone, is_email, is_url, is_clickable,
                                            created_by, updated_by)
        VALUES ('PHONE',     1,  true,  false, false, true,  sys_id, sys_id),
               ('MOBILE',    2,  true,  false, false, true,  sys_id, sys_id),
               ('WHATSAPP',  3,  true,  false, false, true,  sys_id, sys_id),
               ('EMAIL',     4,  false, true,  false, true,  sys_id, sys_id),
               ('WEBSITE',   5,  false, false, true,  true,  sys_id, sys_id),
               ('FACEBOOK',  6,  false, false, true,  true,  sys_id, sys_id),
               ('INSTAGRAM', 7,  false, false, true,  true,  sys_id, sys_id),
               ('X',         8,  false, false, true,  true,  sys_id, sys_id),
               ('LINKEDIN',  9,  false, false, true,  true,  sys_id, sys_id),
               ('TELEGRAM',  10, false, false, false, true,  sys_id, sys_id),
               ('WECHAT',    11, false, false, false, false, sys_id, sys_id),
               ('FAX',       12, true,  false, false, false, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        INSERT INTO communication_channel_locales (communication_channel_id, locale_id, name, description,
                                                   sort_order, created_by, updated_by)
        SELECT c.id, l.id, v.name, v.description, c.sort_order, sys_id, sys_id
        FROM communication_channels c
                 JOIN (VALUES ('PHONE',
                               'Phone',
                               'Fixed-line telephone number for direct calls.'),
                              ('MOBILE',
                               'Mobile',
                               'Mobile phone number for calls and SMS.'),
                              ('WHATSAPP',
                               'WhatsApp',
                               'WhatsApp number for instant messaging and calls.'),
                              ('EMAIL',
                               'Email',
                               'Email address for written correspondence.'),
                              ('WEBSITE',
                               'Website',
                               'Official website URL of the resort.'),
                              ('FACEBOOK',
                               'Facebook',
                               'Facebook page or profile URL.'),
                              ('INSTAGRAM',
                               'Instagram',
                               'Instagram profile URL.'),
                              ('X',
                               'X (Twitter)',
                               'X (formerly Twitter) profile URL.'),
                              ('LINKEDIN',
                               'LinkedIn',
                               'LinkedIn company or profile URL.'),
                              ('TELEGRAM',
                               'Telegram',
                               'Telegram username or group for messaging.'),
                              ('WECHAT',
                               'WeChat',
                               'WeChat ID for messaging.'),
                              ('FAX',
                               'Fax',
                               'Fax number for document transmission.'))
                          v(code, name, description)
                      ON c.code = v.code
                 JOIN locales l ON l.code = 'en'
        ON CONFLICT (communication_channel_id, locale_id) DO NOTHING;
    END
$$;
