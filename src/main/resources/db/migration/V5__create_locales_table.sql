create table if not exists locales
(
    id
               bigserial
        primary
            key,
    code
               varchar(50)                                        not null unique,
    name       varchar(255)                                       not null,
    sort_order integer                  default 0                 not null,
    created_by bigint                                             not null,
    created_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by bigint                                             not null,
    updated_at timestamp with time zone default CURRENT_TIMESTAMP not null,
    version    bigint                   default 0                 not null,
    is_active  boolean                  default true              not null,
    is_deleted boolean                  default false             not null,
    deleted_by bigint,
    deleted_at timestamp with time zone
);

INSERT INTO locales (code,
                     name,
                     sort_order,
                     created_by,
                     updated_by)
VALUES ('en', 'English', 1, 1, 1),
       ('bn', 'Bangla', 2, 1, 1);
