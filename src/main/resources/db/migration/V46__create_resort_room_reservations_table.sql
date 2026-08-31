-- The central, channel-independent booking object. A resort room reservation always books one SPECIFIC
-- resort_room (not just a category) for a date range. It does not store resort_id directly — like resort_rooms
-- (V37), the resort is reached via resort_room_id -> resort_room_categories.resort_id, not duplicated as its
-- own column here.
--
-- No customer_id here — the customer (the booker/payer) is owned entirely by resort_bookings (V45); a resort
-- room reservation is reached via resort_booking_id -> resort_bookings.customer_id, never duplicated as its
-- own column on this table. The manual reservation flow looks the customer up by email (user_emails, V1.1) or
-- phone (user_phones, V1.2); if none matches, it registers a new user, generates a password, and sends the
-- login credentials to whichever of email/phone/WhatsApp is available — that user then owns the booking (and
-- every resort room reservation under it).
--
-- Who is actually staying in this room is a separate concept from the booking's customer: one person can book
-- several rooms for their friends/family in a single booking, and a room can hold more than one occupant. See
-- V47__create_resort_room_reservation_guests_table.sql — each occupant's name is its own row there, not a
-- column here.
--
-- No reservation_source_id here — which channel a booking originated from (WHATSAPP, WEBSITE, OTA, ...) is
-- owned exclusively by resort_bookings.booking_source_id (V43/V45); a resort room reservation resolves its
-- channel by reaching through resort_booking_id, not by duplicating its own source column. "Who/what created
-- this specific row in our system" is the standard created_by audit column below (a booker's user id for
-- manual channels, or the seeded 'system' user for anything created by an automated integration).
create extension if not exists btree_gist;

create table if not exists resort_room_reservations
(
    id                                  bigserial primary key,

    -- Always populated, even for a lone single-room booking (a "group of one") — see
    -- V45__create_resort_bookings_table.sql's own comment above.
    resort_booking_id                   bigint references resort_bookings (id)      not null,
    resort_room_id                      bigint references resort_rooms (id)         not null,

    reservation_status_id               bigint references reservation_statuses (id) not null,

    check_in                            date                                        not null,
    check_out                           date                                        not null,

    -- Append-only status history: nothing ever mutates a resort room reservation row's own status in place.
    -- Instead the old row is soft-deleted (freeing the room, since excl_resort_room_reservations_no_overlap
    -- below only guards is_deleted = false rows) and a new row is inserted with the new status, linked back to
    -- the row it supersedes here — so a booking's full status history can always be walked backward from its
    -- latest (active) row. Null on a resort room reservation's very first row (nothing preceded it).
    previous_resort_room_reservation_id bigint references resort_room_reservations (id),

    adult_count                         integer                                     not null default 1,
    child_count                         integer                                     not null default 0,

    -- price snapshot at booking time — frozen so later changes to resort_room_prices don't retroactively
    -- change what an existing guest agreed to pay.
    currency_id                         bigint references currencies (id)           not null,
    price_unit_id                       bigint references price_units (id)          not null,
    total_price                         numeric(12, 2)                              not null,

    notes                               text                                        not null default '',

    -- Free-text reason captured on the transition that set the CURRENT status of this row — e.g. why a
    -- CANCELLED or NO_SHOW row was marked that way. Null on every row whose status was never explained (most
    -- PENDING/CONFIRMED/CHECKED_IN/CHECKED_OUT rows). Not carried forward from the row a transition supersedes —
    -- each row's own reason describes only the transition that produced it.
    cancellation_reason                 text                                        not null default '',

    -- true for statuses that occupy the room (PENDING, CONFIRMED, CHECKED_IN); false for statuses that free it
    -- (CANCELLED, NO_SHOW, CHECKED_OUT). Kept in sync by fn_sync_resort_room_reservation_blocks_availability
    -- below — never set directly by the application — so the exclusion constraint further down always sees a
    -- correct value.
    blocks_availability                 boolean                                     not null default true,

    created_by                          bigint references users (id)                not null,
    created_at                          timestamp with time zone                    not null default current_timestamp,
    updated_by                          bigint references users (id)                not null,
    updated_at                          timestamp with time zone                    not null default current_timestamp,
    version                             bigint                                      not null default 0,
    is_active                           boolean                                     not null default true,
    is_deleted                          boolean                                     not null default false,
    deleted_by                          bigint references users (id),
    deleted_at                          timestamp with time zone,

    constraint chk_resort_room_reservations_dates
        check (check_out > check_in),
    constraint chk_resort_room_reservations_adult_count
        check (adult_count >= 1),
    constraint chk_resort_room_reservations_child_count
        check (child_count >= 0),
    constraint chk_resort_room_reservations_total_price
        check (total_price >= 0),
    -- A row can be superseded by at most one later row — keeps the status history a straight line, never a fork.
    constraint uq_resort_room_reservations_previous_reservation
        unique (previous_resort_room_reservation_id)
);

create index if not exists idx_resort_room_reservations_room_dates
    on resort_room_reservations (resort_room_id, check_in, check_out);

create index if not exists idx_resort_room_reservations_booking
    on resort_room_reservations (resort_booking_id);

create or replace function fn_sync_resort_room_reservation_blocks_availability()
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

create trigger trg_sync_resort_room_reservation_blocks_availability
    before insert or update of reservation_status_id
    on resort_room_reservations
    for each row
execute function fn_sync_resort_room_reservation_blocks_availability();

-- Prevents overbooking atomically at the database level: two concurrent transactions inserting/updating
-- overlapping date ranges for the same resort_room will have the second one rejected by Postgres itself, so
-- there is no "check availability, then insert" race window for the application to get wrong. Only rows that
-- currently block availability (see trigger above) and are not soft-deleted participate.
alter table resort_room_reservations
    add constraint excl_resort_room_reservations_no_overlap
        exclude using gist (
        resort_room_id with =,
        daterange(check_in, check_out, '[)') with &&
        )
        where (blocks_availability = true and is_deleted = false);
