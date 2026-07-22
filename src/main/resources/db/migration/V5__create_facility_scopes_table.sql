create table if not exists facility_scopes
(
    id         bigserial primary key,

    code       varchar(50)              not null unique,
    sort_order integer                  not null default 0,

    created_by bigint                   not null references users (id),
    created_at timestamp with time zone not null default current_timestamp,
    updated_by bigint                   not null references users (id),
    updated_at timestamp with time zone not null default current_timestamp,
    version    bigint                   not null default 0,
    is_active  boolean                  not null default true,
    is_deleted boolean                  not null default false,
    deleted_by bigint references users (id),
    deleted_at timestamp with time zone
);

create table if not exists facility_scope_locales
(
    id                bigserial primary key,

    facility_scope_id bigint                   not null
        references facility_scopes (id)
            on delete cascade,

    locale_id         bigint                   not null
        references locales (id),

    name              varchar(100)             not null,
    description       text                     not null default '',
    sort_order        integer                  not null default 0,

    created_by        bigint                   not null references users (id),
    created_at        timestamp with time zone not null default current_timestamp,
    updated_by        bigint                   not null references users (id),
    updated_at        timestamp with time zone not null default current_timestamp,
    version           bigint                   not null default 0,
    is_active         boolean                  not null default true,
    is_deleted        boolean                  not null default false,
    deleted_by        bigint references users (id),
    deleted_at        timestamp with time zone,

    constraint uq_facility_scope_locale
        unique (facility_scope_id, locale_id)
);

DO
$$
    DECLARE
        sys_id bigint;
    BEGIN
        SELECT id INTO sys_id FROM users WHERE username = 'system';

        -- =============================================
        -- 1. Facility Scopes
        -- =============================================
        INSERT INTO facility_scopes (code, sort_order, created_by, updated_by)
        VALUES ('RESORT', 1, sys_id, sys_id),
               ('ROOM_CATEGORY', 2, sys_id, sys_id),
               ('ROOM', 3, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        -- =============================================
        -- 2. Facility Scope Locales (English)
        -- =============================================
        INSERT INTO facility_scope_locales (facility_scope_id, locale_id, name, sort_order, created_by, updated_by)
        SELECT fs.id, l.id, v.name, fs.sort_order, sys_id, sys_id
        FROM facility_scopes fs
                 JOIN (VALUES ('RESORT', 'en', 'Resort'),
                              ('ROOM_CATEGORY', 'en', 'Room Category'),
                              ('ROOM', 'en', 'Room')) v(code, locale_code, name)
                      ON fs.code = v.code
                 JOIN locales l ON l.code = v.locale_code
        ON CONFLICT (facility_scope_id, locale_id) DO NOTHING;

    END
$$;