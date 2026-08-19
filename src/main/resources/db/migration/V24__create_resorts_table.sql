create table if not exists resorts
(
    id         bigserial primary key,

    -- Internal unique code identifying the resort.
    code       varchar(100)                 not null,

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

create unique index if not exists uq_resorts_code
    on resorts (code)
    where is_active = true and is_deleted = false;

create table if not exists resort_users
(
    id                  bigserial primary key,

    resort_id           bigint references resorts (id) on delete cascade not null,
    user_id             bigint references users (id) on delete restrict  not null,
    -- Default role for this membership; explicit overrides live in resort_user_permissions.
    resort_role_type_id bigint references resort_role_types (id)         not null,

    joined_at           timestamp with time zone                         not null default current_timestamp,

    created_by          bigint references users (id)                     not null,
    created_at          timestamp with time zone                         not null default current_timestamp,
    updated_by          bigint references users (id)                     not null,
    updated_at          timestamp with time zone                         not null default current_timestamp,
    version             bigint                                           not null default 0,
    is_active           boolean                                          not null default true,
    is_deleted          boolean                                          not null default false,
    deleted_by          bigint references users (id),
    deleted_at          timestamp with time zone
);

create unique index if not exists uq_resort_user
    on resort_users (resort_id, user_id)
    where is_active = true and is_deleted = false;

create table if not exists resort_user_permissions
(
    id                        bigserial primary key,

    resort_user_id            bigint references resort_users (id) on delete cascade not null,
    resort_permission_type_id bigint references resort_permission_types (id)        not null,

    -- true = explicitly allow
    -- false = explicitly deny
    is_allowed                boolean                                               not null,

    created_by                bigint references users (id)                          not null,
    created_at                timestamp with time zone                              not null default current_timestamp,
    updated_by                bigint references users (id)                          not null,
    updated_at                timestamp with time zone                              not null default current_timestamp,
    version                   bigint                                                not null default 0,
    is_active                 boolean                                               not null default true,
    is_deleted                boolean                                               not null default false,
    deleted_by                bigint references users (id),
    deleted_at                timestamp with time zone
);

create unique index if not exists uq_resort_user_permission
    on resort_user_permissions (resort_user_id, resort_permission_type_id)
    where is_active = true and is_deleted = false;
