create table if not exists facility_scopes
(
    id          bigserial primary key,

    code        varchar(50)                   not null unique,
    sort_order  integer                       not null default 0,

    created_by  bigint references users (id) not null,
    created_at  timestamp with time zone      not null default current_timestamp,
    updated_by  bigint references users (id) not null,
    updated_at  timestamp with time zone      not null default current_timestamp,
    version     bigint                        not null default 0,
    is_active   boolean                       not null default true,
    is_deleted  boolean                       not null default false,
    deleted_by  bigint references users (id),
    deleted_at  timestamp with time zone
);

create table if not exists facility_scope_locales
(
    id                 bigserial primary key,

    facility_scope_id  bigint references facility_scopes (id) not null,
    locale_id          bigint references locales (id)         not null,

    name               varchar(100)                            not null,
    description        text                                    not null default '',
    sort_order         integer                                 not null default 0,

    created_by         bigint references users (id)            not null,
    created_at         timestamp with time zone                not null default current_timestamp,
    updated_by         bigint references users (id)            not null,
    updated_at         timestamp with time zone                not null default current_timestamp,
    version            bigint                                  not null default 0,
    is_active          boolean                                 not null default true,
    is_deleted         boolean                                 not null default false,
    deleted_by         bigint references users (id),
    deleted_at         timestamp with time zone,

    constraint uq_facility_scope_locale
        unique (facility_scope_id, locale_id)
);

insert into facility_scopes (code, sort_order, created_by, updated_by)
values ('RESORT', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('ROOM_CATEGORY', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('ROOM', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

insert into facility_scope_locales (facility_scope_id, locale_id, name, sort_order, created_by, updated_by)
values ((select id from facility_scopes where code = 'RESORT'), (select id from locales where code = 'en'),
        'Resort', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ((select id from facility_scopes where code = 'ROOM_CATEGORY'), (select id from locales where code = 'en'),
        'Room Category', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ((select id from facility_scopes where code = 'ROOM'), (select id from locales where code = 'en'),
        'Room', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system'));