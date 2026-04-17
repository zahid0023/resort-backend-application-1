create table if not exists room_price_periods
(
    id          bigserial primary key,
    room_id     bigint                                             not null references rooms (id),
    start_date  date                                               not null,
    end_date    date                                               not null,
    price       numeric(12, 2)                                     not null,
    priority    integer                  default 1                 not null,
    price_type  bigint                                             not null references price_types (id),
    created_by  bigint                                             not null,
    created_at  timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by  bigint                                             not null,
    updated_at  timestamp with time zone default CURRENT_TIMESTAMP not null,
    version     bigint                   default 0                 not null,
    is_active   boolean                  default true              not null,
    is_deleted  boolean                  default false             not null,
    deleted_by  bigint,
    deleted_at  timestamp with time zone
);
