create table if not exists mail_provider_configs
(
    id               bigserial primary key,

    mail_provider_id bigint references mail_providers (id) not null,

    -- e.g. "System Notifications", "Marketing"
    name             varchar(100)                          not null,

    -- Resolves which config a system flow should use to send a specific kind of email, e.g.
    -- CREATE_USER_EMAIL_NOTIFICATIONS. Not every config needs one — only configs backing a system flow do.
    code             varchar(100),

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

create unique index if not exists uq_mail_provider_configs_code
    on mail_provider_configs (code)
    where code is not null and is_active = true and is_deleted = false;
