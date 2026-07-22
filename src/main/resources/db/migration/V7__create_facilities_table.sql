CREATE TABLE IF NOT EXISTS facilities
(
    id                bigserial PRIMARY KEY,

    code              varchar(100)             NOT NULL,
    sort_order        integer                  NOT NULL DEFAULT 1,

    facility_group_id bigint                   NOT NULL REFERENCES facility_groups (id),

    icon_type         varchar(100)             NOT NULL,
    icon_value        text,
    icon_meta         jsonb,

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

CREATE TABLE IF NOT EXISTS facility_locales
(
    id          bigserial PRIMARY KEY,

    facility_id bigint                   NOT NULL REFERENCES facilities (id),
    locale_id   bigint                   NOT NULL REFERENCES locales (id),

    name        varchar(255)             NOT NULL,
    description text                     NOT NULL DEFAULT '',
    sort_order  integer                  NOT NULL DEFAULT 1,

    created_by  bigint                   NOT NULL REFERENCES users (id),
    created_at  timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by  bigint                   NOT NULL REFERENCES users (id),
    updated_at  timestamp with time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version     bigint                   NOT NULL DEFAULT 0,
    is_active   boolean                  NOT NULL DEFAULT true,
    is_deleted  boolean                  NOT NULL DEFAULT false,
    deleted_by  bigint,
    deleted_at  timestamp with time zone
);

-- Seed: facilities
INSERT INTO facilities (code, sort_order, facility_group_id, icon_type, icon_value, icon_meta, created_by, updated_by)
SELECT t.code,
       t.sort_order,
       fg.id,
       t.icon_type::varchar,
       t.icon_value,
       t.icon_meta::jsonb,
       1,
       1
FROM (VALUES ('RESTAURANT', 1, 'DINING', 'LUCIDE', 'UtensilsCrossed', '{"size": 24, "color": "#f59e0b"}'),
             ('POOL_BAR', 2, 'DINING', 'LUCIDE', 'GlassWater', '{"size": 24, "color": "#3b82f6"}'),
             ('SPA', 1, 'WELLNESS', 'LUCIDE', 'Sparkles', '{"size": 24, "color": "#8b5cf6"}'),
             ('FITNESS', 2, 'WELLNESS', 'LUCIDE', 'Dumbbell', '{"size": 24, "color": "#ec4899"}'),
             ('SWIMMING_POOL', 1, 'RECREATION', 'LUCIDE', 'Waves', '{"size": 24, "color": "#06b6d4"}'),
             ('TENNIS', 2, 'RECREATION', 'LUCIDE', 'CircleDot', '{"size": 24, "color": "#84cc16"}'),
             ('ROOM_SERVICE', 1, 'ACCOMMODATION', 'LUCIDE', 'BellRing', '{"size": 24, "color": "#f97316"}'),
             ('CONCIERGE', 2, 'ACCOMMODATION', 'LUCIDE', 'ConciergeBell',
              '{"size": 24, "color": "#14b8a6"}')) AS t(code, sort_order, group_code, icon_type, icon_value, icon_meta)
         JOIN facility_groups fg ON fg.code = t.group_code;

-- Seed: facility locales (English)
INSERT INTO facility_locales (facility_id, locale_id, name, description, sort_order, created_by, updated_by)
SELECT f.id, (SELECT id FROM locales WHERE code = 'en'), t.name_en, t.desc_en, t.sort_order, 1, 1
FROM (VALUES ('RESTAURANT', 'Main Restaurant', 'Full-service restaurant with buffet and à la carte options.', 1),
             ('POOL_BAR', 'Pool Bar', 'Outdoor bar serving refreshments by the pool.', 2),
             ('SPA', 'Spa & Treatment', 'Relaxing spa treatments and beauty therapies.', 1),
             ('FITNESS', 'Fitness Center', 'Fully equipped gym with cardio and strength training.', 2),
             ('SWIMMING_POOL', 'Swimming Pool', 'Outdoor infinity pool with sun deck and loungers.', 1),
             ('TENNIS', 'Tennis Court', 'Floodlit tennis courts available for day and evening play.', 2),
             ('ROOM_SERVICE', 'Room Service', '24-hour in-room dining available for all guests.', 1),
             ('CONCIERGE', 'Concierge', 'Dedicated concierge team for reservations and local guidance.',
              2)) AS t(code, name_en, desc_en, sort_order)
         JOIN facilities f ON f.code = t.code;

create table if not exists facility_scope_assignments
(
    facility_id       bigint                   not null
        references facilities (id)
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
    deleted_at        timestamp with time zone,
    primary key (facility_id, facility_scope_id)
);

-- Seed: facility scope assignments
INSERT INTO facility_scope_assignments (facility_id, facility_scope_id, created_by, updated_by)
SELECT f.id, fs.id, 1, 1
FROM (VALUES ('RESTAURANT', 'RESORT'),
             ('POOL_BAR', 'RESORT'),
             ('SPA', 'RESORT'),
             ('SPA', 'ROOM_CATEGORY'),
             ('FITNESS', 'RESORT'),
             ('SWIMMING_POOL', 'RESORT'),
             ('SWIMMING_POOL', 'ROOM_CATEGORY'),
             ('TENNIS', 'RESORT'),
             ('ROOM_SERVICE', 'ROOM_CATEGORY'),
             ('ROOM_SERVICE', 'ROOM'),
             ('CONCIERGE', 'RESORT'),
             ('CONCIERGE', 'ROOM_CATEGORY')) AS t(facility_code, scope_code)
         JOIN facilities f ON f.code = t.facility_code
         JOIN facility_scopes fs ON fs.code = t.scope_code;