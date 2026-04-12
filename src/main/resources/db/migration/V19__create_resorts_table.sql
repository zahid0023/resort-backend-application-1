create table if not exists resorts
(
    id            bigserial primary key,
    uuid          uuid                                               not null,
    name          varchar(255)                                       not null,
    description   text                     default ''               not null,
    address       text,
    country_id    bigint                                             not null references countries (id),
    city_id       bigint references cities (id),
    contact_email varchar(255),
    contact_phone varchar(50),
    created_by    bigint                                             not null,
    created_at    timestamp with time zone default CURRENT_TIMESTAMP not null,
    updated_by    bigint                                             not null,
    updated_at    timestamp with time zone default CURRENT_TIMESTAMP not null,
    version       bigint                   default 0                 not null,
    is_active     boolean                  default true              not null,
    is_deleted    boolean                  default false             not null,
    deleted_by    bigint,
    deleted_at    timestamp with time zone
);
