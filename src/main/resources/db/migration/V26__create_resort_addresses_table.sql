create table if not exists resort_addresses
(
    id          bigserial primary key,

    resort_id   bigint references resorts (id)   not null,

    country_id  bigint references countries (id) not null,
    city_id     bigint references cities (id)    not null,

    postal_code varchar(50),

    lat         double precision,
    lon         double precision,

    created_by  bigint references users (id)     not null,
    created_at  timestamp with time zone         not null default current_timestamp,
    updated_by  bigint references users (id)     not null,
    updated_at  timestamp with time zone         not null default current_timestamp,
    version     bigint                           not null default 0,
    is_active   boolean                          not null default true,
    is_deleted  boolean                          not null default false,
    deleted_by  bigint references users (id),
    deleted_at  timestamp with time zone
);

create unique index if not exists uq_resort_addresses_resort
    on resort_addresses (resort_id)
    where is_active = true and is_deleted = false;

create table if not exists resort_address_locales
(
    id                bigserial primary key,

    resort_address_id bigint references resort_addresses (id) not null,
    locale_id         bigint references locales (id)          not null,

    address           text                                    not null,
    sort_order        integer                                 not null default 0,

    created_by        bigint references users (id)            not null,
    created_at        timestamp with time zone                not null default current_timestamp,
    updated_by        bigint references users (id)            not null,
    updated_at        timestamp with time zone                not null default current_timestamp,
    version           bigint                                  not null default 0,
    is_active         boolean                                 not null default true,
    is_deleted        boolean                                 not null default false,
    deleted_by        bigint references users (id),
    deleted_at        timestamp with time zone
);

create unique index if not exists uq_resort_address_locale
    on resort_address_locales (resort_address_id, locale_id)
    where is_active = true and is_deleted = false;