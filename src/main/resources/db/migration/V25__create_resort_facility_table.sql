create table if not exists resort_facility
(
    id                        bigserial primary key,
    resort_facility_group_id  bigint                                             not null references resort_facility_group (id),
    facility_id               bigint                                             not null references facility (id),
    name                      varchar(255),
    description               text,
    icon                      varchar(255),
    value                     varchar(255),
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
