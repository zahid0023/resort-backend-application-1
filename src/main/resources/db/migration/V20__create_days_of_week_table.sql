create table if not exists days_of_week
(
    id         bigserial primary key,

    -- Internal code.
    -- Examples:
    -- MONDAY
    -- TUESDAY
    -- WEDNESDAY
    -- THURSDAY
    -- FRIDAY
    -- SATURDAY
    -- SUNDAY
    code       varchar(50)                  not null,
    -- display order in admin ui.
    sort_order integer                      not null default 0,

    created_by bigint references users (id) not null,
    created_at timestamp with time zone     not null default current_timestamp,
    updated_by bigint references users (id) not null,
    updated_at timestamp with time zone     not null default current_timestamp,
    version    bigint                       not null default 0,
    is_active  boolean                      not null default true,
    is_deleted boolean                      not null default false,
    deleted_by bigint references users (id),
    deleted_at timestamp with time zone
);

create unique index if not exists uq_days_of_week_code
    on days_of_week (code)
    where is_active = true and is_deleted = false;

create table if not exists days_of_week_locales
(
    id             bigserial primary key,

    day_of_week_id bigint references days_of_week (id) not null,
    locale_id      bigint references locales (id)      not null,

    name           varchar(100)                        not null,
    short_name     varchar(20)                         not null default '',
    description    text                                not null default '',
    -- display order in admin ui.
    sort_order     integer                             not null default 0,

    created_by     bigint references users (id)        not null,
    created_at     timestamp with time zone            not null default current_timestamp,
    updated_by     bigint references users (id)        not null,
    updated_at     timestamp with time zone            not null default current_timestamp,
    version        bigint                              not null default 0,
    is_active      boolean                             not null default true,
    is_deleted     boolean                             not null default false,
    deleted_by     bigint references users (id),
    deleted_at     timestamp with time zone
);

create unique index if not exists uq_days_of_week_locale
    on days_of_week_locales (day_of_week_id, locale_id)
    where is_active = true and is_deleted = false;

-- seed: days of week
insert into days_of_week (code, sort_order, created_by, updated_by)
values ('MONDAY', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('TUESDAY', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('WEDNESDAY', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('THURSDAY', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('FRIDAY', 5, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('SATURDAY', 6, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('SUNDAY', 7, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: days of week locales (english)
insert into days_of_week_locales (day_of_week_id, locale_id, name, short_name, sort_order, created_by, updated_by)
values ((select id from days_of_week where code = 'MONDAY'), (select id from locales where code = 'en'),
        'Monday', 'Mon', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ((select id from days_of_week where code = 'TUESDAY'), (select id from locales where code = 'en'),
        'Tuesday', 'Tue', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ((select id from days_of_week where code = 'WEDNESDAY'), (select id from locales where code = 'en'),
        'Wednesday', 'Wed', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ((select id from days_of_week where code = 'THURSDAY'), (select id from locales where code = 'en'),
        'Thursday', 'Thu', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ((select id from days_of_week where code = 'FRIDAY'), (select id from locales where code = 'en'),
        'Friday', 'Fri', 5, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ((select id from days_of_week where code = 'SATURDAY'), (select id from locales where code = 'en'),
        'Saturday', 'Sat', 6, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ((select id from days_of_week where code = 'SUNDAY'), (select id from locales where code = 'en'),
        'Sunday', 'Sun', 7, (select id from users where username = 'system'),
        (select id from users where username = 'system'));
