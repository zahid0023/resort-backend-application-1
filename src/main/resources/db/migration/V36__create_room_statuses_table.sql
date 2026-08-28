create table if not exists room_statuses
(
    id         bigserial primary key,

    -- Internal code.
    -- Examples:
    -- AVAILABLE
    -- MAINTENANCE
    -- OUT_OF_ORDER
    -- RENOVATION
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

create unique index if not exists uq_room_statuses_code
    on room_statuses (code)
    where is_active = true and is_deleted = false;

create table if not exists room_status_locales
(
    id             bigserial primary key,

    room_status_id bigint references room_statuses (id) not null,
    locale_id      bigint references locales (id)       not null,

    -- localized display name.
    -- Examples:
    -- Available
    -- Under Maintenance
    -- Out of Order
    -- Under Renovation
    name           varchar(100)                         not null,
    description    text                                 not null default '',
    -- display order in admin ui.
    sort_order     integer                              not null default 0,

    created_by     bigint references users (id)         not null,
    created_at     timestamp with time zone             not null default current_timestamp,
    updated_by     bigint references users (id)         not null,
    updated_at     timestamp with time zone             not null default current_timestamp,
    version        bigint                               not null default 0,
    is_active      boolean                              not null default true,
    is_deleted     boolean                              not null default false,
    deleted_by     bigint references users (id),
    deleted_at     timestamp with time zone
);

create unique index if not exists uq_room_status_locale
    on room_status_locales (room_status_id, locale_id)
    where is_active = true and is_deleted = false;

-- seed: room statuses
insert into room_statuses (code, sort_order, created_by, updated_by)
values ('AVAILABLE', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('MAINTENANCE', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('OUT_OF_ORDER', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('RENOVATION', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: room status locales (english)
insert into room_status_locales (room_status_id, locale_id, name, description, sort_order,
                                 created_by, updated_by)
values ((select id from room_statuses where code = 'AVAILABLE'), (select id from locales where code = 'en'),
        'Available', 'The room is in normal condition and can be booked.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from room_statuses where code = 'MAINTENANCE'),
        (select id from locales where code = 'en'),
        'Under Maintenance', 'The room is temporarily unavailable for routine upkeep or repairs.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from room_statuses where code = 'OUT_OF_ORDER'),
        (select id from locales where code = 'en'),
        'Out of Order', 'The room is unusable due to damage or a fault and cannot be booked.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from room_statuses where code = 'RENOVATION'),
        (select id from locales where code = 'en'),
        'Under Renovation', 'The room is closed for renovation work and cannot be booked.', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system'));
