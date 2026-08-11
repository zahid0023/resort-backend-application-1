create table if not exists bed_types
(
    id         bigserial primary key,

    -- Internal code.
    -- Examples:
    -- SINGLE
    -- TWIN
    -- DOUBLE
    -- QUEEN
    -- KING
    -- SOFA
    -- BUNK
    -- FUTON
    -- MURPHY
    code       varchar(50)                  not null unique,
    -- display order in admin ui.
    sort_order integer                      not null default 0,

    created_by bigint references users (id) not null,
    created_at timestamp with time zone     not null default current_timestamp,
    updated_by bigint references users (id) not null,
    updated_at timestamp with time zone     not null default current_timestamp,
    version    bigint                       not null default 0,
    is_active  boolean                      not null default true,
    is_deleted boolean                      not null default false,
    deleted_by bigint references users (id),
    deleted_at timestamp with time zone
);

create table if not exists bed_type_locales
(
    id          bigserial primary key,

    bed_type_id bigint references bed_types (id) not null,
    locale_id   bigint references locales (id)   not null,

    -- localized display name.
    -- Examples:
    -- Single Bed
    -- Twin Bed
    -- Double Bed
    -- Queen Bed
    -- King Bed
    -- Sofa Bed
    -- Bunk Bed
    -- Futon
    -- Murphy Bed
    name        varchar(100)                     not null,
    description text                             not null default '',
    -- display order in admin ui.
    sort_order  integer                          not null default 0,

    created_by  bigint references users (id)     not null,
    created_at  timestamp with time zone         not null default current_timestamp,
    updated_by  bigint references users (id)     not null,
    updated_at  timestamp with time zone         not null default current_timestamp,
    version     bigint                           not null default 0,
    is_active   boolean                          not null default true,
    is_deleted  boolean                          not null default false,
    deleted_by  bigint references users (id),
    deleted_at  timestamp with time zone,

    constraint uq_bed_type_locale
        unique (bed_type_id, locale_id)
);

-- seed: bed types
insert into bed_types (code, sort_order, created_by, updated_by)
values ('SINGLE', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('TWIN', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('DOUBLE', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('QUEEN', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('KING', 5, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('SOFA', 6, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('BUNK', 7, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('FUTON', 8, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('MURPHY', 9, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: bed type locales (english)
insert into bed_type_locales (bed_type_id, locale_id, name, description, sort_order, created_by, updated_by)
values ((select id from bed_types where code = 'SINGLE'), (select id from locales where code = 'en'),
        'Single Bed', 'A bed designed for one person, typically 90 x 190 cm.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from bed_types where code = 'TWIN'), (select id from locales where code = 'en'),
        'Twin Bed', 'Two separate single beds in the same room, each typically 90 x 190 cm.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from bed_types where code = 'DOUBLE'), (select id from locales where code = 'en'),
        'Double Bed', 'A bed wide enough for two people, typically 135 x 190 cm.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from bed_types where code = 'QUEEN'), (select id from locales where code = 'en'),
        'Queen Bed', 'A large bed suitable for two people, typically 150 x 200 cm.', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from bed_types where code = 'KING'), (select id from locales where code = 'en'),
        'King Bed', 'The largest standard bed size, typically 180 x 200 cm.', 5,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from bed_types where code = 'SOFA'), (select id from locales where code = 'en'),
        'Sofa Bed', 'A sofa that folds out into a sleeping surface, ideal for flexible use.', 6,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from bed_types where code = 'BUNK'), (select id from locales where code = 'en'),
        'Bunk Bed', 'Two beds stacked vertically, commonly used in dormitories or family rooms.', 7,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from bed_types where code = 'FUTON'), (select id from locales where code = 'en'),
        'Futon', 'A low Japanese-style bed or mattress that can be rolled up when not in use.', 8,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from bed_types where code = 'MURPHY'), (select id from locales where code = 'en'),
        'Murphy Bed', 'A wall bed that folds into the wall to save floor space when not in use.', 9,
        (select id from users where username = 'system'), (select id from users where username = 'system'));
