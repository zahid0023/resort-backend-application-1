create table if not exists price_units
(
    id         bigserial primary key,

    -- Stable business code used internally by the application.
    -- Examples:
    -- PER_NIGHT
    -- PER_DAY
    -- PER_HOUR
    -- PER_PERSON
    -- PER_ROOM
    -- PER_BOOKING
    code       varchar(50)                  not null,
    -- Display order in administrative interfaces.
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

create unique index if not exists uq_price_units_code
    on price_units (code)
    where is_active = true and is_deleted = false;

create table if not exists price_unit_locales
(
    id            bigserial primary key,

    -- Parent price unit.
    price_unit_id bigint references price_units (id) not null,

    -- Translation language.
    locale_id     bigint references locales (id)     not null,

    -- Localized display name.
    -- Example: "Per Night"
    name          varchar(100)                       not null,
    -- Short explanation shown in UI.
    description   text                               not null default '',
    -- Display order.
    sort_order    integer                            not null default 0,

    -- Business meaning of this pricing unit.
    purpose       text                               not null default '',

    -- Example scenario shown to administrators.
    usage_example text                               not null default '',

    created_by    bigint references users (id)       not null,
    created_at    timestamp with time zone           not null default current_timestamp,
    updated_by    bigint references users (id)       not null,
    updated_at    timestamp with time zone           not null default current_timestamp,
    version       bigint                             not null default 0,
    is_active     boolean                            not null default true,
    is_deleted    boolean                            not null default false,
    deleted_by    bigint references users (id),
    deleted_at    timestamp with time zone
);

create unique index if not exists uq_price_unit_locale
    on price_unit_locales (price_unit_id, locale_id)
    where is_active = true and is_deleted = false;

create table if not exists price_unit_scope_assignments
(
    id             bigserial primary key,

    price_scope_id bigint references price_scopes (id) not null,
    price_unit_id  bigint references price_units (id)  not null,

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

-- seed: price units
insert into price_units (code, sort_order, created_by, updated_by)
values ('PER_NIGHT', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('PER_DAY', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('PER_HOUR', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('PER_PERSON', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('PER_ROOM', 5, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('PER_BOOKING', 6, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: price unit locales (english)
insert into price_unit_locales (price_unit_id, locale_id, name, description, sort_order, purpose, usage_example,
                                created_by, updated_by)
values ((select id from price_units where code = 'PER_NIGHT'), (select id from locales where code = 'en'),
        'Per Night', 'Price applied once for each night of the stay.', 1,
        'Standard nightly rate used for most room bookings.',
        'A deluxe room costs $120 per night, so a 3-night stay totals $360.',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from price_units where code = 'PER_DAY'), (select id from locales where code = 'en'),
        'Per Day', 'Price applied once for each calendar day, regardless of overnight stay.', 2,
        'Used for day-use bookings or facilities billed by the day rather than the night.',
        'A meeting room costs $200 per day for a full-day conference booking.',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from price_units where code = 'PER_HOUR'), (select id from locales where code = 'en'),
        'Per Hour', 'Price applied for each hour of usage.', 3,
        'Used for short-duration bookings such as facility or equipment rentals.',
        'A tennis court costs $15 per hour, so a 2-hour booking totals $30.',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from price_units where code = 'PER_PERSON'), (select id from locales where code = 'en'),
        'Per Person', 'Price applied once for each guest included in the booking.', 4,
        'Used when cost scales with the number of guests rather than the room or duration.',
        'A buffet dinner costs $25 per person, so a booking for 4 guests totals $100.',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from price_units where code = 'PER_ROOM'), (select id from locales where code = 'en'),
        'Per Room', 'Price applied once for each room booked, regardless of occupancy.', 5,
        'Used when cost is tied to the room itself rather than guest count or duration.',
        'A suite costs $150 per room per night, regardless of how many guests occupy it.',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from price_units where code = 'PER_BOOKING'), (select id from locales where code = 'en'),
        'Per Booking', 'A single flat price applied once to the entire booking.', 6,
        'Used for flat fees that don''t scale with nights, guests, or rooms.',
        'A one-time cleaning fee of $30 is charged per booking, regardless of stay length.',
        (select id from users where username = 'system'), (select id from users where username = 'system'));