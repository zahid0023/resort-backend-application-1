create table if not exists resort_role_types
(
    id         bigserial primary key,

    -- Internal code.
    -- Examples:
    -- OWNER
    -- BOOKER
    code       varchar(100)                 not null unique,
    -- display order in admin ui.
    sort_order integer                      not null default 1,

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

create table if not exists resort_role_type_locales
(
    id                  bigserial primary key,

    resort_role_type_id bigint references resort_role_types (id) not null,
    locale_id           bigint references locales (id)           not null,

    -- localized display name.
    -- Examples:
    -- Owner
    -- Booker
    name                varchar(255)                              not null,
    description         text                                      not null default '',
    -- display order in admin ui.
    sort_order          integer                                   not null default 1,

    created_by          bigint references users (id)              not null,
    created_at          timestamp with time zone                  not null default current_timestamp,
    updated_by          bigint references users (id)              not null,
    updated_at          timestamp with time zone                  not null default current_timestamp,
    version             bigint                                    not null default 0,
    is_active           boolean                                   not null default true,
    is_deleted          boolean                                   not null default false,
    deleted_by          bigint references users (id),
    deleted_at          timestamp with time zone,

    constraint uq_resort_role_type_locale
        unique (resort_role_type_id, locale_id)
);

-- seed: resort role types
insert into resort_role_types (code, sort_order, created_by, updated_by)
values ('OWNER', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('BOOKER', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: resort role type locales (english)
insert into resort_role_type_locales (resort_role_type_id, locale_id, name, description, sort_order,
                                      created_by, updated_by)
values ((select id from resort_role_types where code = 'OWNER'), (select id from locales where code = 'en'),
        'Owner', 'Full control over the resort — can manage settings, staff, and bookings.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from resort_role_types where code = 'BOOKER'), (select id from locales where code = 'en'),
        'Booker', 'Can browse resort details and make reservations on behalf of guests.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system'));