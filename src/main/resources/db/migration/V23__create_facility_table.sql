create table if not exists facility
(
    id                bigserial primary key,
    facility_group_id bigint                                             not null references facility_group (id),
    code              varchar(50)                                        not null,
    name              varchar(255)                                       not null,
    description       text,
    type              varchar(30)                                        not null,
    icon              varchar(255),
    created_by        bigint                                             not null,
    created_at        timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by        bigint                                             not null,
    updated_at        timestamp with time zone default CURRENT_TIMESTAMP not null,
    version           bigint                   default 0                 not null,
    is_active         boolean                  default true              not null,
    is_deleted        boolean                  default false             not null,
    deleted_by        bigint,
    deleted_at        timestamp with time zone
);
