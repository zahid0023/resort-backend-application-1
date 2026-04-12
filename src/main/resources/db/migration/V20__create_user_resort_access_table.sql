create table if not exists user_resort_access
(
    id             bigserial primary key,
    user_id        bigint                                             not null references users (id),
    resort_id      bigint                                             not null references resorts (id),
    access_type_id bigint                                             not null references resort_access_types (id),
    created_by     bigint                                             not null,
    created_at     timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by     bigint                                             not null,
    updated_at     timestamp with time zone default CURRENT_TIMESTAMP not null,
    version        bigint                   default 0                 not null,
    is_active      boolean                  default true              not null,
    is_deleted     boolean                  default false             not null,
    deleted_by     bigint,
    deleted_at     timestamp with time zone,
    unique (user_id, resort_id)
);
