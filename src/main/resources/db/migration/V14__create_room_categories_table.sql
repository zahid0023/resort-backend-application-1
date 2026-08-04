create table if not exists room_categories
(
    id         bigserial primary key,

    code       varchar(50)                  not null unique,
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

create table if not exists room_category_locales
(
    id               bigserial primary key,

    room_category_id bigint references room_categories (id) not null,
    locale_id        bigint references locales (id)         not null,

    name             varchar(100)                           not null,
    description      text                                   not null default '',
    sort_order       integer                                not null default 0,

    created_by       bigint references users (id)           not null,
    created_at       timestamp with time zone               not null default current_timestamp,
    updated_by       bigint references users (id)           not null,
    updated_at       timestamp with time zone               not null default current_timestamp,
    version          bigint                                 not null default 0,
    is_active        boolean                                not null default true,
    is_deleted       boolean                                not null default false,
    deleted_by       bigint references users (id),
    deleted_at       timestamp with time zone,

    constraint uq_room_category_locale
        unique (room_category_id, locale_id)
);

-- seed: room categories
insert into room_categories (code, sort_order, created_by, updated_by)
values ('STD', 1, (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('DLX', 2, (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('STE', 3, (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('FAM', 4, (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('EXE', 5, (select id from users where username = 'system'), (select id from users where username = 'system')),
       ('PRS', 6, (select id from users where username = 'system'), (select id from users where username = 'system'));

-- seed: room category locales (english)
insert into room_category_locales (room_category_id, locale_id, name, description, sort_order, created_by, updated_by)
values ((select id from room_categories where code = 'STD'), (select id from locales where code = 'en'),
        'Standard Room', 'Comfortable room with essential amenities for everyday stays.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from room_categories where code = 'DLX'), (select id from locales where code = 'en'),
        'Deluxe Room', 'Spacious room with upgraded interior and additional facilities.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from room_categories where code = 'STE'), (select id from locales where code = 'en'),
        'Suite', 'Luxury suite featuring separate living and sleeping areas.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from room_categories where code = 'FAM'), (select id from locales where code = 'en'),
        'Family Room', 'Large room designed for families and group accommodations.', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from room_categories where code = 'EXE'), (select id from locales where code = 'en'),
        'Executive Room', 'Premium room designed for business and executive travelers.', 5,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from room_categories where code = 'PRS'), (select id from locales where code = 'en'),
        'Presidential Suite', 'Top-tier luxury suite with exclusive premium services.', 6,
        (select id from users where username = 'system'), (select id from users where username = 'system'));
