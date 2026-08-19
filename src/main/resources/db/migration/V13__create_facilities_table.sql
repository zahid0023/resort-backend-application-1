create table if not exists facilities
(
    id         bigserial primary key,

    code       varchar(100)                 not null,
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

create unique index if not exists uq_facilities_code
    on facilities (code)
    where is_active = true and is_deleted = false;

create table if not exists facility_group_facility_assignments
(
    id                bigserial primary key,

    facility_group_id bigint references facility_groups (id) not null,
    facility_id       bigint references facilities (id)      not null,

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

create table if not exists facility_locales
(
    id          bigserial primary key,

    facility_id bigint references facilities (id) not null,
    locale_id   bigint references locales (id)    not null,

    name        varchar(255)                      not null,
    description text                              not null default '',
    sort_order  integer                           not null default 0,

    created_by  bigint references users (id)      not null,
    created_at  timestamp with time zone          not null default current_timestamp,
    updated_by  bigint references users (id)      not null,
    updated_at  timestamp with time zone          not null default current_timestamp,
    version     bigint                            not null default 0,
    is_active   boolean                           not null default true,
    is_deleted  boolean                           not null default false,
    deleted_by  bigint,
    deleted_at  timestamp with time zone
);

create table if not exists facility_scope_assignments
(
    id                bigserial primary key,

    facility_scope_id bigint references facility_scopes (id) not null,
    facility_id       bigint references facilities (id)      not null,

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

-- seed: facilities
insert into facilities (code, sort_order, icon_type, icon_value, icon_meta, created_by, updated_by)
values ('RESTAURANT', 1, 'LUCIDE', 'UtensilsCrossed',
        '{
          "size": 24,
          "color": "#f59e0b"
        }',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('POOL_BAR', 2, 'LUCIDE', 'GlassWater',
        '{
          "size": 24,
          "color": "#3b82f6"
        }',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('SPA', 1, 'LUCIDE', 'Sparkles',
        '{
          "size": 24,
          "color": "#8b5cf6"
        }',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('FITNESS', 2, 'LUCIDE', 'Dumbbell',
        '{
          "size": 24,
          "color": "#ec4899"
        }',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('SWIMMING_POOL', 1, 'LUCIDE', 'Waves',
        '{
          "size": 24,
          "color": "#06b6d4"
        }',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('TENNIS', 2, 'LUCIDE', 'CircleDot',
        '{
          "size": 24,
          "color": "#84cc16"
        }',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('ROOM_SERVICE', 1, 'LUCIDE', 'BellRing',
        '{
          "size": 24,
          "color": "#f97316"
        }',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('CONCIERGE', 2, 'LUCIDE', 'ConciergeBell',
        '{
          "size": 24,
          "color": "#14b8a6"
        }',
        (select id from users where username = 'system'), (select id from users where username = 'system'));

-- seed: facility group facility assignments
insert into facility_group_facility_assignments (facility_group_id, facility_id, created_by, updated_by)
values ((select id from facility_groups where code = 'DINING'), (select id from facilities where code = 'RESTAURANT'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'DINING'), (select id from facilities where code = 'POOL_BAR'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'WELLNESS'), (select id from facilities where code = 'SPA'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'WELLNESS'), (select id from facilities where code = 'FITNESS'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'RECREATION'),
        (select id from facilities where code = 'SWIMMING_POOL'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'RECREATION'), (select id from facilities where code = 'TENNIS'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'ACCOMMODATION'),
        (select id from facilities where code = 'ROOM_SERVICE'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_groups where code = 'ACCOMMODATION'),
        (select id from facilities where code = 'CONCIERGE'),
        (select id from users where username = 'system'), (select id from users where username = 'system'));

-- seed: facility locales (english)
insert into facility_locales (facility_id, locale_id, name, description, sort_order, created_by, updated_by)
values ((select id from facilities where code = 'RESTAURANT'), (select id from locales where code = 'en'),
        'Main Restaurant', 'Full-service restaurant with buffet and à la carte options.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'POOL_BAR'), (select id from locales where code = 'en'),
        'Pool Bar', 'Outdoor bar serving refreshments by the pool.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'SPA'), (select id from locales where code = 'en'),
        'Spa & Treatment', 'Relaxing spa treatments and beauty therapies.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'FITNESS'), (select id from locales where code = 'en'),
        'Fitness Center', 'Fully equipped gym with cardio and strength training.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'SWIMMING_POOL'), (select id from locales where code = 'en'),
        'Swimming Pool', 'Outdoor infinity pool with sun deck and loungers.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'TENNIS'), (select id from locales where code = 'en'),
        'Tennis Court', 'Floodlit tennis courts available for day and evening play.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'ROOM_SERVICE'), (select id from locales where code = 'en'),
        'Room Service', '24-hour in-room dining available for all guests.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'CONCIERGE'), (select id from locales where code = 'en'),
        'Concierge', 'Dedicated concierge team for reservations and local guidance.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system'));

-- seed: facility scope assignments
insert into facility_scope_assignments (facility_id, facility_scope_id, created_by, updated_by)
values ((select id from facilities where code = 'RESTAURANT'), (select id from facility_scopes where code = 'RESORT'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'POOL_BAR'), (select id from facility_scopes where code = 'RESORT'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'SPA'), (select id from facility_scopes where code = 'RESORT'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'SPA'), (select id from facility_scopes where code = 'ROOM_CATEGORY'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'FITNESS'), (select id from facility_scopes where code = 'RESORT'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'SWIMMING_POOL'),
        (select id from facility_scopes where code = 'RESORT'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'SWIMMING_POOL'),
        (select id from facility_scopes where code = 'ROOM_CATEGORY'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'TENNIS'), (select id from facility_scopes where code = 'RESORT'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'ROOM_SERVICE'),
        (select id from facility_scopes where code = 'ROOM_CATEGORY'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'ROOM_SERVICE'), (select id from facility_scopes where code = 'ROOM'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'CONCIERGE'), (select id from facility_scopes where code = 'RESORT'),
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facilities where code = 'CONCIERGE'),
        (select id from facility_scopes where code = 'ROOM_CATEGORY'),
        (select id from users where username = 'system'), (select id from users where username = 'system'));
