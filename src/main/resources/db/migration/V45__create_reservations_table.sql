-- The central, channel-independent booking object. A reservation always books one SPECIFIC resort_room (not
-- just a category) for a date range, for one customer. It does not store resort_id directly — like
-- resort_rooms (V37), the resort is reached via resort_room_id -> resort_room_categories.resort_id, not
-- duplicated as its own column here.
--
-- customer_id references users(id) directly — a customer is not a separate table, it's a `users` row with no
-- resort_users membership (see V1.1's note). The manual reservation flow looks the customer up by email
-- (user_emails, V1.1) or phone (user_phones, V1.2); if none matches, it registers a new user, generates a
-- password, and sends the login credentials to whichever of email/phone/WhatsApp is available — that user
-- then owns this reservation and every future one, across any resort.
--
-- Channel vs creator: reservation_source_id answers "where did this booking originate" (WHATSAPP, WEBSITE,
-- OTA, ...). "Who/what created it in our system" is the standard created_by audit column below (a booker's
-- user id for manual channels, or the seeded 'system' user for anything created by an automated integration).
create extension if not exists btree_gist;

-- A customer booking one or more rooms in a single transaction (e.g. "2 Standard rooms, Sep 10-12"). Each room
-- is still its own independent reservations row (own status/price/cancellation), tagged with the same
-- booking_group_id here so they can be queried/shown together as one booking. Every reservation belongs to
-- exactly one booking_group, even a lone single-room booking — a "group of one" — so booking_group_id is
-- always populated (not null), never optional, for consistency across every reservation.
create table if not exists booking_groups
(
    id          bigserial primary key,

    resort_id   bigint references resorts (id) not null,
    customer_id bigint references users (id)    not null,

    created_by  bigint references users (id)    not null,
    created_at  timestamp with time zone         not null default current_timestamp,
    updated_by  bigint references users (id)     not null,
    updated_at  timestamp with time zone         not null default current_timestamp,
    version     bigint                           not null default 0,
    is_active   boolean                          not null default true,
    is_deleted  boolean                          not null default false,
    deleted_by  bigint references users (id),
    deleted_at  timestamp with time zone
);

create index if not exists idx_booking_groups_resort
    on booking_groups (resort_id);

create table if not exists reservations
(
    id                    bigserial primary key,

    customer_id           bigint references users (id)                not null,
    resort_room_id        bigint references resort_rooms (id)         not null,

    -- Always populated, even for a lone single-room booking (a "group of one") — see booking_groups' own
    -- comment above.
    booking_group_id      bigint references booking_groups (id)      not null,

    reservation_status_id bigint references reservation_statuses (id) not null,
    reservation_source_id bigint references reservation_sources (id)  not null,

    check_in              date                                        not null,
    check_out             date                                        not null,

    -- Append-only status history: nothing ever mutates a reservation row's own status in place. Instead the
    -- old row is soft-deleted (freeing the room, since excl_reservations_no_overlap below only guards
    -- is_deleted = false rows) and a new row is inserted with the new status, linked back to the row it
    -- supersedes here — so a booking's full status history can always be walked backward from its latest
    -- (active) row. Null on a reservation's very first row (nothing preceded it).
    previous_reservation_id bigint references reservations (id),

    adult_count           integer                                     not null default 1,
    child_count           integer                                     not null default 0,

    -- price snapshot at booking time — frozen so later changes to resort_room_prices don't retroactively
    -- change what an existing guest agreed to pay.
    currency_id           bigint references currencies (id)           not null,
    price_unit_id         bigint references price_units (id)          not null,
    total_price           numeric(12, 2)                              not null,

    notes                 text                                        not null default '',

    -- true for statuses that occupy the room (PENDING, CONFIRMED, CHECKED_IN); false for statuses that free it
    -- (CANCELLED, NO_SHOW, CHECKED_OUT). Kept in sync by fn_sync_reservation_blocks_availability below — never
    -- set directly by the application — so the exclusion constraint further down always sees a correct value.
    blocks_availability   boolean                                     not null default true,

    created_by            bigint references users (id)                not null,
    created_at            timestamp with time zone                    not null default current_timestamp,
    updated_by            bigint references users (id)                not null,
    updated_at            timestamp with time zone                    not null default current_timestamp,
    version               bigint                                      not null default 0,
    is_active             boolean                                     not null default true,
    is_deleted            boolean                                     not null default false,
    deleted_by            bigint references users (id),
    deleted_at            timestamp with time zone,

    constraint chk_reservations_dates
        check (check_out > check_in),
    constraint chk_reservations_adult_count
        check (adult_count >= 1),
    constraint chk_reservations_child_count
        check (child_count >= 0),
    constraint chk_reservations_total_price
        check (total_price >= 0),
    -- A row can be superseded by at most one later row — keeps the status history a straight line, never a fork.
    constraint uq_reservations_previous_reservation
        unique (previous_reservation_id)
);

create index if not exists idx_reservations_customer
    on reservations (customer_id);

create index if not exists idx_reservations_room_dates
    on reservations (resort_room_id, check_in, check_out);

create index if not exists idx_reservations_booking_group
    on reservations (booking_group_id);

create or replace function fn_sync_reservation_blocks_availability()
    returns trigger as
$$
declare
    v_code varchar(50);
begin
    select code
    into v_code
    from reservation_statuses
    where id = new.reservation_status_id;

    new.blocks_availability := v_code not in ('CANCELLED', 'NO_SHOW', 'CHECKED_OUT');

    return new;
end;
$$ language plpgsql;

create trigger trg_sync_reservation_blocks_availability
    before insert or update of reservation_status_id
    on reservations
    for each row
execute function fn_sync_reservation_blocks_availability();

-- Prevents overbooking atomically at the database level: two concurrent transactions inserting/updating
-- overlapping date ranges for the same resort_room will have the second one rejected by Postgres itself, so
-- there is no "check availability, then insert" race window for the application to get wrong. Only rows that
-- currently block availability (see trigger above) and are not soft-deleted participate.
alter table reservations
    add constraint excl_reservations_no_overlap
        exclude using gist (
        resort_room_id with =,
        daterange(check_in, check_out, '[)') with &&
        )
        where (blocks_availability = true and is_deleted = false);
