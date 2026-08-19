create table if not exists resort_basic_info
(
    id         bigserial primary key,

    resort_id  bigint references resorts (id) not null unique,

    estd       smallint                       not null,

    logo_url   text,

    created_by bigint references users (id)   not null,
    created_at timestamp with time zone       not null default current_timestamp,
    updated_by bigint references users (id)   not null,
    updated_at timestamp with time zone       not null default current_timestamp,
    version    bigint                         not null default 0,
    is_active  boolean                        not null default true,
    is_deleted boolean                        not null default false,
    deleted_by bigint references users (id),
    deleted_at timestamp with time zone
);

create table if not exists resort_basic_info_locales
(
    id                   bigserial primary key,

    resort_basic_info_id bigint references resort_basic_info (id) on delete cascade not null,
    locale_id            bigint references locales (id) on delete restrict          not null,

    name                 varchar(255)                                               not null,
    tagline              text                                                       not null,
    short_description    varchar(1024),

    sort_order           integer                                                    not null default 0,

    created_by           bigint references users (id)                               not null,
    created_at           timestamp with time zone                                   not null default current_timestamp,
    updated_by           bigint references users (id)                               not null,
    updated_at           timestamp with time zone                                   not null default current_timestamp,
    version              bigint                                                     not null default 0,
    is_active            boolean                                                    not null default true,
    is_deleted           boolean                                                    not null default false,
    deleted_by           bigint references users (id),
    deleted_at           timestamp with time zone,

    constraint uq_resort_basic_info_locale
        unique (resort_basic_info_id, locale_id)
);
