CREATE TABLE IF NOT EXISTS room_categories
(
    id         bigserial PRIMARY KEY,

    code       varchar(50)                                        NOT NULL UNIQUE,
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

CREATE TABLE IF NOT EXISTS room_category_locales
(
    id               bigserial PRIMARY KEY,

    room_category_id bigint                                             NOT NULL REFERENCES room_categories (id) ON DELETE CASCADE,
    locale_id        bigint                                             NOT NULL REFERENCES locales (id) ON DELETE CASCADE,

    name             varchar(100)                                       NOT NULL,
    description      text                                               NOT NULL DEFAULT '',
    sort_order       integer                  DEFAULT 0                 NOT NULL,

    created_by       bigint                                             NOT NULL REFERENCES users (id),
    created_at       timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_by       bigint                                             NOT NULL REFERENCES users (id),
    updated_at       timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version          bigint                   DEFAULT 0                 NOT NULL,
    is_active        boolean                  DEFAULT true              NOT NULL,
    is_deleted       boolean                  DEFAULT false             NOT NULL,
    deleted_by       bigint REFERENCES users (id),
    deleted_at       timestamp with time zone,

    CONSTRAINT uq_room_category_locale
        UNIQUE (room_category_id, locale_id)
);

-- =========================================================
-- Seed: room_categories
-- =========================================================

INSERT INTO room_categories
(code,
 sort_order,
 created_by,
 updated_by)
VALUES ('STD', 1, 1, 1),
       ('DLX', 2, 1, 1),
       ('STE', 3, 1, 1),
       ('FAM', 4, 1, 1),
       ('EXE', 5, 1, 1),
       ('PRS', 6, 1, 1);

-- =========================================================
-- Seed: room_category_locales (English)
-- =========================================================

INSERT INTO room_category_locales
(room_category_id,
 locale_id,
 name,
 description,
 sort_order,
 created_by,
 updated_by)
SELECT rc.id,
       l.id,
       t.name_en,
       t.description_en,
       t.sort_order,
       1,
       1
FROM (VALUES ('STANDARD',
              'Standard Room',
              'Comfortable room with essential amenities for everyday stays.',
              1),

             ('DELUXE',
              'Deluxe Room',
              'Spacious room with upgraded interior and additional facilities.',
              2),

             ('SUITE',
              'Suite',
              'Luxury suite featuring separate living and sleeping areas.',
              3),

             ('FAMILY',
              'Family Room',
              'Large room designed for families and group accommodations.',
              4),

             ('EXECUTIVE',
              'Executive Room',
              'Premium room designed for business and executive travelers.',
              5),

             ('PRESIDENTIAL',
              'Presidential Suite',
              'Top-tier luxury suite with exclusive premium services.',
              6)) AS t(code, name_en, description_en, sort_order)

         JOIN room_categories rc
              ON rc.code = t.code

         JOIN locales l
              ON l.code = 'en';