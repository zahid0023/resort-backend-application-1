create table if not exists facility_groups
(
    id         bigserial primary key,

    code       varchar(100)                 not null unique,
    sort_order integer                      not null default 0,

    icon_type  varchar(100)                 not null,
    icon_value text,
    icon_meta  jsonb,

    created_by bigint references users (id) not null,
    created_at timestamp with time zone     not null default current_timestamp,
    updated_by bigint references users (id) not null,
    updated_at timestamp with time zone     not null default current_timestamp,
    version    bigint                       not null default 0,
    is_active  boolean                      not null default true,
    is_deleted boolean                      not null default false,
    deleted_by bigint,
    deleted_at timestamp with time zone
);

create table if not exists facility_group_locales
(
    id                bigserial primary key,

    facility_group_id bigint references facility_groups (id) not null,
    locale_id         bigint references locales (id)         not null,

    name              varchar(255)                           not null,
    description       text                                   not null default '',
    sort_order        integer                                not null default 0,

    created_by        bigint references users (id)           not null,
    created_at        timestamp with time zone               not null default current_timestamp,
    updated_by        bigint references users (id)           not null,
    updated_at        timestamp with time zone               not null default current_timestamp,
    version           bigint                                 not null default 0,
    is_active         boolean                                not null default true,
    is_deleted        boolean                                not null default false,
    deleted_by        bigint,
    deleted_at        timestamp with time zone
);

create table if not exists facility_group_scope_assignments
(
    id                bigserial primary key,

    facility_scope_id bigint references facility_scopes (id) not null,
    facility_group_id bigint references facility_groups (id) not null,

    created_by        bigint references users (id)           not null,
    created_at        timestamp with time zone               not null default current_timestamp,
    updated_by        bigint references users (id)           not null,
    updated_at        timestamp with time zone               not null default current_timestamp,
    version           bigint                                 not null default 0,
    is_active         boolean                                not null default true,
    is_deleted        boolean                                not null default false,
    deleted_by        bigint,
    deleted_at        timestamp with time zone
);

-- seed: facility groups
insert into facility_groups (code, sort_order, icon_type, icon_value, icon_meta, created_by, updated_by)
values ('DINING', 1, 'LUCIDE', 'UtensilsCrossed',
        '{
          "size": 24,
          "color": "#f59e0b",
          "stroke_width": 1.5
        }',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('WELLNESS', 2, 'LUCIDE', 'Spa',
        '{
          "size": 24,
          "color": "#8b5cf6",
          "stroke_width": 1.5
        }',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('RECREATION', 3, 'LUCIDE', 'Waves',
        '{
          "size": 24,
          "color": "#3b82f6",
          "stroke_width": 1.5
        }',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('ACCOMMODATION', 4, 'LUCIDE', 'BedDouble',
        '{
          "size": 24,
          "color": "#10b981",
          "stroke_width": 1.5
        }',
        (select id from users where username = 'system'), (select id from users where username = 'system'));

-- seed: facility group locales (english)
insert into facility_group_locales (facility_group_id, locale_id, name, description, sort_order, created_by, updated_by)
values ((select id from facility_groups where code = 'DINING'), (select id from locales where code = 'en'),
        'Dining', 'All food and beverage outlets including restaurants, bars, and room service.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'WELLNESS'), (select id from locales where code = 'en'),
        'Wellness', 'Spa, fitness center, and wellness treatment facilities.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'RECREATION'), (select id from locales where code = 'en'),
        'Recreation', 'Swimming pools, sports courts, and outdoor leisure activities.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'ACCOMMODATION'), (select id from locales where code = 'en'),
        'Accommodation', 'Room types, suite options, and lodging facilities.', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system'));

-- seed: facility group scope assignments
insert into facility_group_scope_assignments (facility_group_id, facility_scope_id, created_by, updated_by)
values ((select id from facility_groups where code = 'DINING'), (select id from facility_scopes where code = 'RESORT'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'DINING'),
        (select id from facility_scopes where code = 'ROOM_CATEGORY'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'DINING'), (select id from facility_scopes where code = 'ROOM'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'WELLNESS'),
        (select id from facility_scopes where code = 'RESORT'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'WELLNESS'),
        (select id from facility_scopes where code = 'ROOM_CATEGORY'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'RECREATION'),
        (select id from facility_scopes where code = 'RESORT'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'RECREATION'),
        (select id from facility_scopes where code = 'ROOM_CATEGORY'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'ACCOMMODATION'),
        (select id from facility_scopes where code = 'ROOM_CATEGORY'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'ACCOMMODATION'),
        (select id from facility_scopes where code = 'ROOM'),
        (select id from users where username = 'system'), (select id from users where username = 'system'));
