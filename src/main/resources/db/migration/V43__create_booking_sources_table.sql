-- Where a booking originated (the communication/booking channel), independent of who/what created it in
-- the system (that's the standard created_by audit column on `bookings`, e.g. a booker's user id, or the
-- seeded 'system' user for bookings created directly by an automated channel like a future website/OTA
-- integration). Owned exclusively by the booking (see bookings.booking_source_id, V45) — a room reservation
-- resolves its channel by reaching through its booking rather than storing its own.
create table if not exists booking_sources
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

create unique index if not exists uq_booking_sources_code
    on booking_sources (code)
    where is_active = true and is_deleted = false;

create table if not exists booking_source_locales
(
    id                bigserial primary key,

    booking_source_id bigint references booking_sources (id) not null,
    locale_id         bigint references locales (id)         not null,

    -- localized display name.
    -- Examples:
    -- Manual
    -- Website
    -- WhatsApp
    name              varchar(150)                            not null,
    description       text                                    not null default '',
    -- display order in admin ui.
    sort_order        integer                                not null default 0,

    created_by        bigint references users (id)            not null,
    created_at        timestamp with time zone                not null default current_timestamp,
    updated_by        bigint references users (id)            not null,
    updated_at        timestamp with time zone                not null default current_timestamp,
    version           bigint                                  not null default 0,
    is_active         boolean                                 not null default true,
    is_deleted        boolean                                 not null default false,
    deleted_by        bigint references users (id),
    deleted_at        timestamp with time zone
);

create unique index if not exists uq_booking_source_locale
    on booking_source_locales (booking_source_id, locale_id)
    where is_active = true and is_deleted = false;

-- seed: booking sources
insert into booking_sources (code, sort_order, created_by, updated_by)
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

-- seed: booking source locales (english)
insert into booking_source_locales (booking_source_id, locale_id, name, description, sort_order,
                                    created_by, updated_by)
values ((select id from booking_sources where code = 'MANUAL'), (select id from locales where code = 'en'),
        'Manual', 'Booking entered manually by staff without a specific channel.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from booking_sources where code = 'WEBSITE'), (select id from locales where code = 'en'),
        'Website', 'Booking made directly through the resort website.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from booking_sources where code = 'PHONE'), (select id from locales where code = 'en'),
        'Phone', 'Booking requested over a phone call.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from booking_sources where code = 'WHATSAPP'), (select id from locales where code = 'en'),
        'WhatsApp', 'Booking requested over WhatsApp.', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from booking_sources where code = 'FACEBOOK'), (select id from locales where code = 'en'),
        'Facebook', 'Booking requested through Facebook messaging or a Facebook page.', 5,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from booking_sources where code = 'INSTAGRAM'), (select id from locales where code = 'en'),
        'Instagram', 'Booking requested through Instagram messaging.', 6,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from booking_sources where code = 'WALK_IN'), (select id from locales where code = 'en'),
        'Walk-in', 'Booking made in person at the resort with no prior contact.', 7,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from booking_sources where code = 'OTA'), (select id from locales where code = 'en'),
        'OTA', 'Booking made through a third-party online travel agency (e.g. Booking.com, Agoda).', 8,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from booking_sources where code = 'API'), (select id from locales where code = 'en'),
        'API', 'Booking created programmatically through the booking API.', 9,
        (select id from users where username = 'system'), (select id from users where username = 'system'));
