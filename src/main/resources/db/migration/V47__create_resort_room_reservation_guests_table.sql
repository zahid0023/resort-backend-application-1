-- Multiple people can stay in the same room — adult_count/child_count on resort_room_reservations (V46) tracks
-- headcount only, not identity. Each occupant's name is its own row here rather than a single column on
-- resort_room_reservations, split out the same way user_emails/user_phones are split from users (V1.1/V1.2): a
-- resort room reservation can have more than one guest, so this is its own table. A guest is plain free text,
-- not a users(id) reference — unlike the booking's own customer, a guest does not need a platform account,
-- login, or identity resolution.
--
-- guest_type reconciles a named guest with the resort room reservation's own adult_count/child_count: the
-- count of ADULT rows for a resort room reservation should equal its adult_count, and CHILD rows its
-- child_count (not DB-enforced, since that would require a deferred cross-table check — the application is
-- responsible for keeping them in sync).
create type guest_type as enum ('ADULT', 'CHILD');

create table if not exists resort_room_reservation_guests
(
    id                         bigserial primary key,

    resort_room_reservation_id bigint references resort_room_reservations (id) on delete cascade not null,

    name                       varchar(255)                                                      not null,
    guest_type                 guest_type                                                        not null default 'ADULT',
    sort_order                 integer                                                           not null default 0,

    created_by                 bigint references users (id)                                      not null,
    created_at                 timestamp with time zone                                          not null default current_timestamp,
    updated_by                 bigint references users (id)                                      not null,
    updated_at                 timestamp with time zone                                          not null default current_timestamp,
    version                    bigint                                                            not null default 0,
    is_active                  boolean                                                           not null default true,
    is_deleted                 boolean                                                           not null default false,
    deleted_by                 bigint references users (id),
    deleted_at                 timestamp with time zone
);

create index if not exists idx_resort_room_reservation_guests_reservation
    on resort_room_reservation_guests (resort_room_reservation_id);
