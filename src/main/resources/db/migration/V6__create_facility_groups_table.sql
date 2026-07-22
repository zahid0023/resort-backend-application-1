CREATE TABLE IF NOT EXISTS facility_groups
(
    id         bigserial PRIMARY KEY,

    code       varchar(100)             NOT NULL,
    sort_order integer                  NOT NULL DEFAULT 1,

    icon_type  varchar(100)             NOT NULL,
    icon_value text,
    icon_meta  jsonb,

    created_by bigint                   NOT NULL REFERENCES users (id),
    created_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint                   NOT NULL REFERENCES users (id),
    updated_at timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version    bigint                   NOT NULL DEFAULT 0,
    is_active  boolean                  NOT NULL DEFAULT true,
    is_deleted boolean                  NOT NULL DEFAULT false,
    deleted_by bigint,
    deleted_at timestamp with time zone
);

CREATE TABLE IF NOT EXISTS facility_group_locales
(
    id                bigserial PRIMARY KEY,

    facility_group_id bigint                   NOT NULL REFERENCES facility_groups (id),
    locale_id         bigint                   NOT NULL REFERENCES locales (id),

    name              varchar(255)             NOT NULL,
    description       text                     NOT NULL DEFAULT '',
    sort_order        integer                  NOT NULL DEFAULT 1,

    created_by        bigint                   NOT NULL REFERENCES users (id),
    created_at        timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        bigint                   NOT NULL REFERENCES users (id),
    updated_at        timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version           bigint                   NOT NULL DEFAULT 0,
    is_active         boolean                  NOT NULL DEFAULT true,
    is_deleted        boolean                  NOT NULL DEFAULT false,
    deleted_by        bigint,
    deleted_at        timestamp with time zone
);

-- Seed: facility groups
INSERT INTO facility_groups (code, sort_order, icon_type, icon_value, icon_meta, created_by, updated_by)
VALUES ('DINING', 1, 'LUCIDE', 'UtensilsCrossed', '{
  "size": 24,
  "color": "#f59e0b",
  "stroke_width": 1.5
}', 1, 1),
       ('WELLNESS', 2, 'LUCIDE', 'Spa', '{
         "size": 24,
         "color": "#8b5cf6",
         "stroke_width": 1.5
       }', 1, 1),
       ('RECREATION', 3, 'LUCIDE', 'Waves', '{
         "size": 24,
         "color": "#3b82f6",
         "stroke_width": 1.5
       }', 1, 1),
       ('ACCOMMODATION', 4, 'LUCIDE', 'BedDouble', '{
         "size": 24,
         "color": "#10b981",
         "stroke_width": 1.5
       }', 1, 1);

-- Seed: facility group locales (English)
INSERT INTO facility_group_locales (facility_group_id, locale_id, name, description, sort_order, created_by, updated_by)
SELECT fg.id, (SELECT id FROM locales WHERE code = 'en'), t.name_en, t.desc_en, t.sort_order, 1, 1
FROM (VALUES ('DINING', 'Dining', 'All food and beverage outlets including restaurants, bars, and room service.', 1),
             ('WELLNESS', 'Wellness', 'Spa, fitness center, and wellness treatment facilities.', 2),
             ('RECREATION', 'Recreation', 'Swimming pools, sports courts, and outdoor leisure activities.', 3),
             ('ACCOMMODATION', 'Accommodation', 'Room types, suite options, and lodging facilities.',
              4)) AS t(code, name_en, desc_en, sort_order)
         JOIN facility_groups fg ON fg.code = t.code;

create table if not exists facility_group_scope_assignments
(
    facility_group_id bigint                   not null
        references facility_groups (id)
            on delete cascade,

    facility_scope_id bigint                   not null
        references facility_scopes (id)
            on delete cascade,

    created_by        bigint                   NOT NULL REFERENCES users (id),
    created_at        timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by        bigint                   NOT NULL REFERENCES users (id),
    updated_at        timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version           bigint                   NOT NULL DEFAULT 0,
    is_active         boolean                  NOT NULL DEFAULT true,
    is_deleted        boolean                  NOT NULL DEFAULT false,
    deleted_by        bigint,
    primary key (facility_group_id, facility_scope_id)
);

-- Seed: facility group scope assignments
INSERT INTO facility_group_scope_assignments (facility_group_id, facility_scope_id, created_by, updated_by)
SELECT fg.id, fs.id, 1, 1
FROM (VALUES ('DINING', 'RESORT'),
             ('DINING', 'ROOM_CATEGORY'),
             ('DINING', 'ROOM'),
             ('WELLNESS', 'RESORT'),
             ('WELLNESS', 'ROOM_CATEGORY'),
             ('RECREATION', 'RESORT'),
             ('RECREATION', 'ROOM_CATEGORY'),
             ('ACCOMMODATION', 'ROOM_CATEGORY'),
             ('ACCOMMODATION', 'ROOM')) AS t(group_code, scope_code)
         JOIN facility_groups fg ON fg.code = t.group_code
         JOIN facility_scopes fs ON fs.code = t.scope_code;