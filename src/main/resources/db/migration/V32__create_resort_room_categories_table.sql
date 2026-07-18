create table if not exists resort_room_categories
(
    id                     bigserial primary key,

    resort_id              bigint                                             not null references resorts (id) on delete cascade,
    room_category_id       bigint                                             not null references room_categories (id),

    code                   varchar(50)                                        not null,
    sort_order             integer                  default 0                 not null,

    max_adults             integer                  default 2                 not null,
    max_children           integer                  default 0                 not null,
    max_occupancy          integer                  default 2                 not null,

    default_check_in_time  time,
    default_check_out_time time,

    is_extra_bed_allowed   boolean                  default false             not null,
    max_extra_beds         integer                  default 0                 not null,

    is_smoking_allowed     boolean                  default false             not null,
    is_pets_allowed        boolean                  default false             not null,

    created_by             bigint                                             not null references users (id),
    created_at             timestamp with time zone default current_timestamp not null,
    updated_by             bigint                                             not null references users (id),
    updated_at             timestamp with time zone default current_timestamp not null,
    version                bigint                   default 0                 not null,
    is_active              boolean                  default true              not null,
    is_deleted             boolean                  default false             not null,
    deleted_by             bigint references users (id),
    deleted_at             timestamp with time zone,

    constraint uq_resort_room_category
        unique (resort_id, room_category_id),

    constraint uq_resort_room_category_code
        unique (resort_id, code),

    constraint chk_resort_room_category_max_occupancy
        check (max_occupancy >= max_adults + max_children),

    constraint chk_resort_room_category_extra_beds
        check (max_extra_beds >= 0)
);

create table resort_room_category_locales
(
    id                      bigserial primary key,

    resort_room_category_id bigint                                not null
        references resort_room_categories (id)
            on delete cascade,

    locale_id               bigint                                not null
        references locales (id),

    name                    varchar(150)                          not null,
    description             text        default ''                not null,
    sort_order              integer     default 0                 not null,

    created_by              bigint                                not null references users (id),
    created_at              timestamptz default current_timestamp not null,
    updated_by              bigint                                not null references users (id),
    updated_at              timestamptz default current_timestamp not null,
    version                 bigint      default 0                 not null,
    is_active               boolean     default true              not null,
    is_deleted              boolean     default false             not null,
    deleted_by              bigint references users (id),
    deleted_at              timestamptz,

    unique (resort_room_category_id, locale_id)
);