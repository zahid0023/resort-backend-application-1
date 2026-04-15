create table if not exists resort_room_categories
(
    id               bigserial primary key,
    resort_id        bigint                                             not null references resorts (id),
    room_category_id bigint                                             not null references room_categories (id),
    name             varchar(255),
    description      text,
    sort_order       integer,
    created_by       bigint                                             not null,
    created_at       timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by       bigint                                             not null,
    updated_at       timestamp with time zone default CURRENT_TIMESTAMP not null,
    version          bigint                   default 0                 not null,
    is_active        boolean                  default true              not null,
    is_deleted       boolean                  default false             not null,
    deleted_by       bigint,
    deleted_at       timestamp with time zone
);
