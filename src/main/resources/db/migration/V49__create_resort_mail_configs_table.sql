create table if not exists resort_mail_configs
(
    id               bigserial primary key,

    resort_id        bigint references resorts (id)       not null,
    mail_provider_id bigint references mail_providers (id) not null,

    -- e.g. "Bookings Inbox", "Support Inbox"
    name             varchar(100)                          not null,

    config           jsonb                                 not null,

    created_by       bigint references users (id)          not null,
    created_at       timestamp with time zone              not null default current_timestamp,
    updated_by       bigint references users (id)          not null,
    updated_at       timestamp with time zone              not null default current_timestamp,
    version          bigint                                not null default 0,
    is_active        boolean                               not null default true,
    is_deleted       boolean                               not null default false,
    deleted_by       bigint references users (id),
    deleted_at       timestamp with time zone
);

create index if not exists idx_resort_mail_configs_resort
    on resort_mail_configs (resort_id);
