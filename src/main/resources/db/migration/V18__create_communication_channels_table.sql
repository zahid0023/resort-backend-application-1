create table if not exists communication_channels
(
    id           bigserial primary key,

    -- unique internal code used by the application.
    -- examples:
    -- PHONE
    -- MOBILE
    -- EMAIL
    -- WHATSAPP
    -- FAX
    -- WEBSITE
    -- FACEBOOK
    -- INSTAGRAM
    -- X
    -- LINKEDIN
    -- TELEGRAM
    -- WECHAT
    code         varchar(50)                  not null unique,
    -- display order in admin ui.
    sort_order   integer                      not null default 0,

    -- whether this channel expects a url.
    -- example: WEBSITE, FACEBOOK, INSTAGRAM.
    is_url       boolean                      not null default false,
    -- whether this channel represents a phone number.
    -- example: PHONE, MOBILE, WHATSAPP.
    is_phone     boolean                      not null default false,
    -- whether this channel represents an email address.
    -- example: EMAIL.
    is_email     boolean                      not null default false,
    -- whether the value should be clickable in ui.
    is_clickable boolean                      not null default true,

    created_by   bigint references users (id) not null,
    created_at   timestamp with time zone     not null default current_timestamp,
    updated_by   bigint references users (id) not null,
    updated_at   timestamp with time zone     not null default current_timestamp,
    version      bigint                       not null default 0,
    is_active    boolean                      not null default true,
    is_deleted   boolean                      not null default false,
    deleted_by   bigint references users (id),
    deleted_at   timestamp with time zone
);

create table if not exists communication_channel_locales
(
    id                       bigserial primary key,

    -- parent communication channel.
    communication_channel_id bigint references communication_channels (id) not null,
    -- translation language.
    locale_id                bigint references locales (id) not null,

    -- localized display name.
    -- examples:
    -- english : phone
    -- bangla  : ফোন
    name                     varchar(100)                 not null,
    -- localized description.
    description              text                         not null default '',
    -- display order.
    sort_order               integer                      not null default 0,

    created_by               bigint references users (id) not null,
    created_at               timestamp with time zone     not null default current_timestamp,
    updated_by               bigint references users (id) not null,
    updated_at               timestamp with time zone     not null default current_timestamp,
    version                  bigint                       not null default 0,
    is_active                boolean                      not null default true,
    is_deleted               boolean                      not null default false,
    deleted_by               bigint references users (id),
    deleted_at               timestamp with time zone,

    constraint uq_communication_channel_locale
        unique (communication_channel_id, locale_id)
);

-- seed: communication channels
insert into communication_channels (code, sort_order, is_url, is_phone, is_email, is_clickable, created_by, updated_by)
values ('PHONE', 1, false, true, false, true, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('MOBILE', 2, false, true, false, true, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('WHATSAPP', 3, false, true, false, true, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('EMAIL', 4, false, false, true, true, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('WEBSITE', 5, true, false, false, true, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('FACEBOOK', 6, true, false, false, true, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('INSTAGRAM', 7, true, false, false, true, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('X', 8, true, false, false, true, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('LINKEDIN', 9, true, false, false, true, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('TELEGRAM', 10, false, false, false, true, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('WECHAT', 11, false, false, false, false, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('FAX', 12, false, true, false, false, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: communication channel locales (english)
insert into communication_channel_locales (communication_channel_id, locale_id, name, description, sort_order,
                                            created_by, updated_by)
values ((select id from communication_channels where code = 'PHONE'), (select id from locales where code = 'en'),
        'Phone', 'Fixed-line telephone number for direct calls.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from communication_channels where code = 'MOBILE'), (select id from locales where code = 'en'),
        'Mobile', 'Mobile phone number for calls and SMS.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from communication_channels where code = 'WHATSAPP'), (select id from locales where code = 'en'),
        'WhatsApp', 'WhatsApp number for instant messaging and calls.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from communication_channels where code = 'EMAIL'), (select id from locales where code = 'en'),
        'Email', 'Email address for written correspondence.', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from communication_channels where code = 'WEBSITE'), (select id from locales where code = 'en'),
        'Website', 'Official website URL of the resort.', 5,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from communication_channels where code = 'FACEBOOK'), (select id from locales where code = 'en'),
        'Facebook', 'Facebook page or profile URL.', 6,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from communication_channels where code = 'INSTAGRAM'), (select id from locales where code = 'en'),
        'Instagram', 'Instagram profile URL.', 7,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from communication_channels where code = 'X'), (select id from locales where code = 'en'),
        'X (Twitter)', 'X (formerly Twitter) profile URL.', 8,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from communication_channels where code = 'LINKEDIN'), (select id from locales where code = 'en'),
        'LinkedIn', 'LinkedIn company or profile URL.', 9,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from communication_channels where code = 'TELEGRAM'), (select id from locales where code = 'en'),
        'Telegram', 'Telegram username or group for messaging.', 10,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from communication_channels where code = 'WECHAT'), (select id from locales where code = 'en'),
        'WeChat', 'WeChat ID for messaging.', 11,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from communication_channels where code = 'FAX'), (select id from locales where code = 'en'),
        'Fax', 'Fax number for document transmission.', 12,
        (select id from users where username = 'system'), (select id from users where username = 'system'));
