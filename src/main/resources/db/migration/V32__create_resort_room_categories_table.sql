create table if not exists resort_room_categories
(
    id               bigserial primary key,

    resort_id        bigint references resorts (id)         not null,
    room_category_id bigint references room_categories (id) not null,

    -- resort-scoped identifier, unique per resort.
    code             varchar(50)                            not null,
    sort_order       integer                                not null default 0,

    created_by       bigint references users (id)           not null,
    created_at       timestamp with time zone               not null default current_timestamp,
    updated_by       bigint references users (id)           not null,
    updated_at       timestamp with time zone               not null default current_timestamp,
    version          bigint                                 not null default 0,
    is_active        boolean                                not null default true,
    is_deleted       boolean                                not null default false,
    deleted_by       bigint references users (id),
    deleted_at       timestamp with time zone
);

create unique index if not exists uq_resort_room_category
    on resort_room_categories (resort_id, room_category_id)
    where is_active = true and is_deleted = false;

create unique index if not exists uq_resort_room_category_code
    on resort_room_categories (resort_id, code)
    where is_active = true and is_deleted = false;

create table if not exists resort_room_category_locales
(
    id                      bigserial primary key,

    resort_room_category_id bigint references resort_room_categories (id) not null,
    locale_id               bigint references locales (id)                not null,

    name                    varchar(150)                                  not null,
    description             text                                          not null default '',
    sort_order              integer                                       not null default 0,

    created_by              bigint references users (id)                  not null,
    created_at              timestamp with time zone                      not null default current_timestamp,
    updated_by              bigint references users (id)                  not null,
    updated_at              timestamp with time zone                      not null default current_timestamp,
    version                 bigint                                        not null default 0,
    is_active               boolean                                       not null default true,
    is_deleted              boolean                                       not null default false,
    deleted_by              bigint references users (id),
    deleted_at              timestamp with time zone
);

create unique index if not exists uq_resort_room_category_locale
    on resort_room_category_locales (resort_room_category_id, locale_id)
    where is_active = true and is_deleted = false;

create table if not exists resort_room_category_metas
(
    id                      bigserial primary key,

    resort_room_category_id bigint references resort_room_categories (id) not null,

    -- occupancy
    max_adults              integer                                       not null default 2,
    max_children            integer                                       not null default 0,
    max_infants             integer                                       not null default 0,
    max_occupancy           integer                                       not null default 2,

    -- room details
    room_size               numeric(10, 2),
    room_size_unit_id       bigint references units (id),
    bedroom_count           integer                                       not null default 1,
    bathroom_count          integer                                       not null default 1,

    -- booking rules
    minimum_stay_nights     integer                                       not null default 1,
    maximum_stay_nights     integer,

    created_by              bigint references users (id)                  not null,
    created_at              timestamp with time zone                      not null default current_timestamp,
    updated_by              bigint references users (id)                  not null,
    updated_at              timestamp with time zone                      not null default current_timestamp,
    version                 bigint                                        not null default 0,
    is_active               boolean                                       not null default true,
    is_deleted              boolean                                       not null default false,
    deleted_by              bigint references users (id),
    deleted_at              timestamp with time zone,

    constraint chk_resort_room_category_meta_occupancy
        check (max_occupancy >= max_adults + max_children + max_infants),
    constraint chk_resort_room_category_meta_room_size
        check (room_size is null or room_size > 0),
    constraint chk_resort_room_category_meta_bedroom_count
        check (bedroom_count > 0),
    constraint chk_resort_room_category_meta_bathroom_count
        check (bathroom_count > 0),
    constraint chk_resort_room_category_meta_minimum_stay
        check (minimum_stay_nights > 0),
    constraint chk_resort_room_category_meta_maximum_stay
        check (maximum_stay_nights is null or maximum_stay_nights >= minimum_stay_nights)
);

create unique index if not exists uq_resort_room_category_meta
    on resort_room_category_metas (resort_room_category_id)
    where is_active = true and is_deleted = false;

create table if not exists resort_room_category_beds
(
    id                      bigserial primary key,

    resort_room_category_id bigint references resort_room_categories (id) not null,
    bed_type_id             bigint references bed_types (id)              not null,

    quantity                integer                                       not null default 1,

    -- extra bed
    is_extra_bed_allowed    boolean                                       not null default false,
    max_extra_beds          integer                                       not null default 0,

    created_by              bigint references users (id)                  not null,
    created_at              timestamp with time zone                      not null default current_timestamp,
    updated_by              bigint references users (id)                  not null,
    updated_at              timestamp with time zone                      not null default current_timestamp,
    version                 bigint                                        not null default 0,
    is_active               boolean                                       not null default true,
    is_deleted              boolean                                       not null default false,
    deleted_by              bigint references users (id),
    deleted_at              timestamp with time zone,

    constraint chk_resort_room_category_bed_quantity
        check (quantity > 0),
    constraint chk_resort_room_category_meta_extra_beds
        check (max_extra_beds >= 0)
);

create unique index if not exists uq_resort_room_category_bed
    on resort_room_category_beds (resort_room_category_id, bed_type_id)
    where is_active = true and is_deleted = false;
