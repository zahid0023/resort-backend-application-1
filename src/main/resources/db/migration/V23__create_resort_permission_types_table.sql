create table if not exists resort_permission_types
(
    id         bigserial primary key,

    -- Internal code.
    -- Examples:
    -- ALL_PERMISSIONS
    -- VIEW_BOOKING
    -- CREATE_BOOKING
    -- CANCEL_BOOKING
    -- MANAGE_ROOMS
    -- VIEW_REPORTS
    -- MANAGE_STAFF
    code       varchar(100)                 not null unique,
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

create table if not exists resort_permission_type_locales
(
    id                        bigserial primary key,

    resort_permission_type_id bigint references resort_permission_types (id) not null,
    locale_id                 bigint references locales (id)                 not null,

    -- localized display name.
    -- Examples:
    -- All Permissions
    -- View Bookings
    -- Create Bookings
    name                      varchar(255)                                   not null,
    description               text                                           not null default '',
    -- display order in admin ui.
    sort_order                integer                                        not null default 0,

    created_by                bigint references users (id)                   not null,
    created_at                timestamp with time zone                       not null default current_timestamp,
    updated_by                bigint references users (id)                   not null,
    updated_at                timestamp with time zone                       not null default current_timestamp,
    version                   bigint                                         not null default 0,
    is_active                 boolean                                        not null default true,
    is_deleted                boolean                                        not null default false,
    deleted_by                bigint references users (id),
    deleted_at                timestamp with time zone,

    constraint uq_resort_permission_type_locale
        unique (resort_permission_type_id, locale_id)
);

-- seed: resort permission types
insert into resort_permission_types (code, sort_order, created_by, updated_by)
values ('ALL_PERMISSIONS', 0, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('VIEW_BOOKING', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('CREATE_BOOKING', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('CANCEL_BOOKING', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('MANAGE_ROOMS', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('VIEW_REPORTS', 5, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('MANAGE_STAFF', 6, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: resort permission type locales (english)
insert into resort_permission_type_locales (resort_permission_type_id, locale_id, name, description, sort_order,
                                            created_by, updated_by)
values ((select id from resort_permission_types where code = 'ALL_PERMISSIONS'),
        (select id from locales where code = 'en'),
        'All Permissions', 'Grants full access to all resort operations.', 0,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from resort_permission_types where code = 'VIEW_BOOKING'),
        (select id from locales where code = 'en'),
        'View Bookings', 'Can view all reservations and booking details for the resort.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from resort_permission_types where code = 'CREATE_BOOKING'),
        (select id from locales where code = 'en'),
        'Create Bookings', 'Can create new reservations on behalf of guests.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from resort_permission_types where code = 'CANCEL_BOOKING'),
        (select id from locales where code = 'en'),
        'Cancel Bookings', 'Can cancel existing reservations.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from resort_permission_types where code = 'MANAGE_ROOMS'),
        (select id from locales where code = 'en'),
        'Manage Rooms', 'Can manage room availability, pricing, and configuration.', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from resort_permission_types where code = 'VIEW_REPORTS'),
        (select id from locales where code = 'en'),
        'View Reports', 'Can access occupancy, revenue, and operational reports.', 5,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from resort_permission_types where code = 'MANAGE_STAFF'),
        (select id from locales where code = 'en'),
        'Manage Staff', 'Can assign and revoke resort access for other users.', 6,
        (select id from users where username = 'system'), (select id from users where username = 'system'));
