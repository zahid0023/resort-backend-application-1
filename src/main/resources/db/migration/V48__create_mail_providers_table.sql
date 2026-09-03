create table if not exists mail_providers
(
    id          bigserial primary key,

    -- GMAIL
    -- REPLIT
    -- CUSTOM_SMTP
    code        varchar(50)                  not null,

    name        varchar(100)                 not null,
    description text                         not null default '',
    sort_order  integer                      not null default 0,

    created_by  bigint references users (id) not null,
    created_at  timestamp with time zone     not null default current_timestamp,
    updated_by  bigint references users (id) not null,
    updated_at  timestamp with time zone     not null default current_timestamp,
    version     bigint                       not null default 0,
    is_active   boolean                      not null default true,
    is_deleted  boolean                      not null default false,
    deleted_by  bigint references users (id),
    deleted_at  timestamp with time zone
);

create unique index if not exists uq_mail_providers_code
    on mail_providers (code)
    where is_active = true and is_deleted = false;

create table if not exists mail_provider_config_fields
(
    id               bigserial primary key,

    mail_provider_id bigint references mail_providers (id) not null,

    key              varchar(100)                          not null,
    label            varchar(100)                          not null,
    -- TEXT
    -- PASSWORD
    -- NUMBER
    -- BOOLEAN
    -- URL
    field_type       varchar(30)                           not null,

    placeholder      varchar(255)                          not null default '',
    default_value    varchar(500)                          not null default '',
    is_required      boolean                               not null default true,

    sort_order       integer                               not null default 0,

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

create unique index if not exists uq_mail_provider_key
    on mail_provider_config_fields (mail_provider_id, key)
    where is_active = true and is_deleted = false;

insert into mail_providers (code, name, description, sort_order, created_by, updated_by)
values ('GMAIL', 'Gmail', '', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('REPLIT', 'Replit Mail', '', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('CUSTOM_SMTP', 'Custom SMTP', '', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

insert into mail_provider_config_fields (mail_provider_id, key, label, field_type, placeholder, default_value,
                                         is_required, sort_order, created_by, updated_by)
values
-- GMAIL
((select id from mail_providers where code = 'GMAIL'), 'host', 'SMTP Host', 'TEXT', '', 'smtp.gmail.com', true, 1,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'GMAIL'), 'port', 'SMTP Port', 'NUMBER', '', '587', true, 2,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'GMAIL'), 'username', 'Username', 'TEXT', 'you@gmail.com', '', true, 3,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'GMAIL'), 'password', 'App Password', 'PASSWORD',
 'Gmail App Password, not your account password', '', true, 4,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'GMAIL'), 'fromName', 'From Name', 'TEXT', '', '', true, 5,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'GMAIL'), 'fromEmail', 'From Email', 'TEXT', '', '', true, 6,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'GMAIL'), 'useTls', 'Use STARTTLS', 'BOOLEAN', '', 'true', false, 7,
 (select id from users where username = 'system'), (select id from users where username = 'system')),

-- REPLIT
((select id from mail_providers where code = 'REPLIT'), 'host', 'SMTP Host', 'TEXT', 'smtp.replit.com', '', true, 1,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'REPLIT'), 'port', 'SMTP Port', 'NUMBER', '587', '', true, 2,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'REPLIT'), 'username', 'Username', 'TEXT', '', '', true, 3,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'REPLIT'), 'password', 'Password', 'PASSWORD', '', '', true, 4,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'REPLIT'), 'fromName', 'From Name', 'TEXT', '', '', true, 5,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'REPLIT'), 'fromEmail', 'From Email', 'TEXT', '', '', true, 6,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'REPLIT'), 'useTls', 'Use STARTTLS', 'BOOLEAN', '', 'true', false, 7,
 (select id from users where username = 'system'), (select id from users where username = 'system')),

-- CUSTOM_SMTP
((select id from mail_providers where code = 'CUSTOM_SMTP'), 'host', 'SMTP Host', 'TEXT', 'smtp.example.com', '',
 true, 1, (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'CUSTOM_SMTP'), 'port', 'SMTP Port', 'NUMBER', '587', '', true, 2,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'CUSTOM_SMTP'), 'username', 'Username', 'TEXT', '', '', true, 3,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'CUSTOM_SMTP'), 'password', 'Password', 'PASSWORD', '', '', true, 4,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'CUSTOM_SMTP'), 'fromName', 'From Name', 'TEXT', '', '', true, 5,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'CUSTOM_SMTP'), 'fromEmail', 'From Email', 'TEXT', '', '', true, 6,
 (select id from users where username = 'system'), (select id from users where username = 'system')),
((select id from mail_providers where code = 'CUSTOM_SMTP'), 'useTls', 'Use STARTTLS', 'BOOLEAN', '', 'true', false,
 7, (select id from users where username = 'system'), (select id from users where username = 'system'));
