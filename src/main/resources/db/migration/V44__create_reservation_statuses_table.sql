-- Reservation lifecycle status. `blocks_availability` on `reservations` (V45) is derived from this table's
-- code via trigger, so the codes below are relied on by name, not just by seed convenience — renaming a code
-- requires updating fn_sync_reservation_blocks_availability in V45 too.
create table if not exists reservation_statuses
(
    id         bigserial primary key,

    -- Internal code.
    -- Examples:
    -- PENDING
    -- CONFIRMED
    -- CHECKED_IN
    -- CHECKED_OUT
    -- CANCELLED
    -- NO_SHOW
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

create unique index if not exists uq_reservation_statuses_code
    on reservation_statuses (code)
    where is_active = true and is_deleted = false;

create table if not exists reservation_status_locales
(
    id                    bigserial primary key,

    reservation_status_id bigint references reservation_statuses (id) not null,
    locale_id             bigint references locales (id)              not null,

    -- localized display name.
    -- Examples:
    -- Pending
    -- Confirmed
    -- Checked In
    name                  varchar(150)                                 not null,
    description           text                                        not null default '',
    -- display order in admin ui.
    sort_order            integer                                     not null default 0,

    created_by            bigint references users (id)                 not null,
    created_at            timestamp with time zone                     not null default current_timestamp,
    updated_by            bigint references users (id)                 not null,
    updated_at            timestamp with time zone                     not null default current_timestamp,
    version               bigint                                       not null default 0,
    is_active             boolean                                      not null default true,
    is_deleted            boolean                                      not null default false,
    deleted_by            bigint references users (id),
    deleted_at            timestamp with time zone
);

create unique index if not exists uq_reservation_status_locale
    on reservation_status_locales (reservation_status_id, locale_id)
    where is_active = true and is_deleted = false;

-- seed: reservation statuses
insert into reservation_statuses (code, sort_order, created_by, updated_by)
values ('PENDING', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('CONFIRMED', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('CHECKED_IN', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('CHECKED_OUT', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('CANCELLED', 5, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('NO_SHOW', 6, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: reservation status locales (english)
insert into reservation_status_locales (reservation_status_id, locale_id, name, description, sort_order,
                                        created_by, updated_by)
values ((select id from reservation_statuses where code = 'PENDING'), (select id from locales where code = 'en'),
        'Pending', 'Reservation has been created but is not yet confirmed.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_statuses where code = 'CONFIRMED'), (select id from locales where code = 'en'),
        'Confirmed', 'Reservation is confirmed and the room is held for the guest.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_statuses where code = 'CHECKED_IN'), (select id from locales where code = 'en'),
        'Checked In', 'Guest has arrived and checked into the room.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_statuses where code = 'CHECKED_OUT'), (select id from locales where code = 'en'),
        'Checked Out', 'Guest has completed their stay and checked out.', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_statuses where code = 'CANCELLED'), (select id from locales where code = 'en'),
        'Cancelled', 'Reservation was cancelled before check-in.', 5,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from reservation_statuses where code = 'NO_SHOW'), (select id from locales where code = 'en'),
        'No Show', 'Guest did not arrive for the reservation.', 6,
        (select id from users where username = 'system'), (select id from users where username = 'system'));
