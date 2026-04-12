create table if not exists facility_group
(
    id          bigserial primary key,
    code        varchar(50)                                        not null,
    name        varchar(255)                                       not null,
    description text,
    sort_order  integer                  default 0                 not null,
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
