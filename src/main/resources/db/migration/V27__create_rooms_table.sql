create table if not exists rooms
(
    id                        bigserial primary key,
    resort_room_category_id   bigint                                             not null references resort_room_categories (id),
    name                      varchar(255)                                       not null,
    description               text                     default ''               not null,
    room_number               varchar(50),
    floor                     integer,
    max_adults                integer                                            not null,
    max_children              integer                  default 0                 not null,
    max_occupancy             integer,
    base_price                numeric(12, 2)                                     not null,
    created_by                bigint                                             not null,
    created_at                timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by                bigint                                             not null,
    updated_at                timestamp with time zone default CURRENT_TIMESTAMP not null,
    version                   bigint                   default 0                 not null,
    is_active                 boolean                  default true              not null,
    is_deleted                boolean                  default false             not null,
    deleted_by                bigint,
    deleted_at                timestamp with time zone
);
