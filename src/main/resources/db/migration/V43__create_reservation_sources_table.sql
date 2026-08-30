-- Where a reservation originated (the communication/booking channel), independent of who/what created it in
-- the system (that's the standard created_by audit column on `reservations`, e.g. a booker's user id, or the
-- seeded 'system' user for reservations created directly by an automated channel like a future website/OTA
-- integration).
create table if not exists reservation_sources
(
    id         bigserial primary key,

    -- Internal code.
    -- Examples:
    -- MANUAL
    -- WEBSITE
    -- PHONE
    -- WHATSAPP
    -- FACEBOOK
    -- INSTAGRAM
    -- WALK_IN
    -- OTA
    -- API
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

create unique index if not exists uq_reservation_sources_code
    on reservation_sources (code)
    where is_active = true and is_deleted = false;

create table if not exists reservation_source_locales
(
    id                    bigserial primary key,

    reservation_source_id bigint references reservation_sources (id) not null,
    locale_id             bigint references locales (id)             not null,

    -- localized display name.
    -- Examples:
    -- Manual
    -- Website
    -- WhatsApp
    name                  varchar(150)                                not null,
    description           text                                       not null default '',
    -- display order in admin ui.
    sort_order            integer                                    not null default 0,

    created_by            bigint references users (id)                not null,
    created_at            timestamp with time zone                    not null default current_timestamp,
    updated_by            bigint references users (id)                not null,
    updated_at            timestamp with time zone                    not null default current_timestamp,
    version               bigint                                      not null default 0,
    is_active             boolean                                     not null default true,
    is_deleted            boolean                                     not null default false,
    deleted_by            bigint references users (id),
    deleted_at            timestamp with time zone
);

create unique index if not exists uq_reservation_source_locale
    on reservation_source_locales (reservation_source_id, locale_id)
    where is_active = true and is_deleted = false;

-- seed: reservation sources
insert into reservation_sources (code, sort_order, created_by, updated_by)
values ('MANUAL', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('WEBSITE', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('PHONE', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('WHATSAPP', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('FACEBOOK', 5, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('INSTAGRAM', 6, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('WALK_IN', 7, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('OTA', 8, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('API', 9, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: reservation source locales (english)
insert into reservation_source_locales (reservation_source_id, locale_id, name, description, sort_order,
                                        created_by, updated_by)
values ((select id from reservation_sources where code = 'MANUAL'), (select id from locales where code = 'en'),
        'Manual', 'Reservation entered manually by staff without a specific channel.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_sources where code = 'WEBSITE'), (select id from locales where code = 'en'),
        'Website', 'Reservation made directly through the resort website.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_sources where code = 'PHONE'), (select id from locales where code = 'en'),
        'Phone', 'Reservation requested over a phone call.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_sources where code = 'WHATSAPP'), (select id from locales where code = 'en'),
        'WhatsApp', 'Reservation requested over WhatsApp.', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_sources where code = 'FACEBOOK'), (select id from locales where code = 'en'),
        'Facebook', 'Reservation requested through Facebook messaging or a Facebook page.', 5,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_sources where code = 'INSTAGRAM'), (select id from locales where code = 'en'),
        'Instagram', 'Reservation requested through Instagram messaging.', 6,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_sources where code = 'WALK_IN'), (select id from locales where code = 'en'),
        'Walk-in', 'Reservation made in person at the resort with no prior contact.', 7,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_sources where code = 'OTA'), (select id from locales where code = 'en'),
        'OTA', 'Reservation made through a third-party online travel agency (e.g. Booking.com, Agoda).', 8,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_sources where code = 'API'), (select id from locales where code = 'en'),
        'API', 'Reservation created programmatically through the reservation API.', 9,
        (select id from users where username = 'system'), (select id from users where username = 'system'));
