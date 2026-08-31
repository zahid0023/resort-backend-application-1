-- A customer booking one or more rooms in a single transaction (e.g. "2 Standard rooms, Sep 10-12"). Each room
-- is still its own independent resort_room_reservations row (own status/price/cancellation, see
-- V46__create_resort_room_reservations_table.sql), tagged with the same resort_booking_id here so they can be
-- queried/shown together as one booking. Every room reservation belongs to exactly one booking, even a lone
-- single-room booking — a "group of one" — so resort_booking_id is always populated (not null), never
-- optional, for consistency across every room reservation.
create sequence if not exists resort_booking_reference_code_seq;

create table if not exists resort_bookings
(
    id                bigserial primary key,

    resort_id         bigint references resorts (id)         not null,
    customer_id       bigint references users (id)           not null,

    -- Which channel this whole booking originated from (WHATSAPP/PHONE/WEBSITE/OTA/etc., see V43) — owned
    -- exclusively by the booking, never duplicated per room reservation; a room reservation resolves its
    -- channel by reaching through resort_booking_id, not by storing its own booking_source_id.
    booking_source_id bigint references booking_sources (id) not null,

    notes             text                                   not null default '',

    -- Human-readable code (e.g. "BK00000123") a customer can quote back over phone/WhatsApp instead of this
    -- row's raw numeric id. Drawn from resort_booking_reference_code_seq and formatted by the application
    -- before insert (see ResortBookingServiceImpl#create) rather than derived from the row's own id or a DB
    -- trigger — the id isn't known until after insert, and computing it application-side avoids a
    -- same-transaction staleness trap where a later read within the same request would see the pre-insert
    -- in-memory value instead of a trigger-populated one.
    reference_code    varchar(20)                            not null,

    created_by        bigint references users (id)           not null,
    created_at        timestamp with time zone               not null default current_timestamp,
    updated_by        bigint references users (id)           not null,
    updated_at        timestamp with time zone               not null default current_timestamp,
    version           bigint                                 not null default 0,
    is_active         boolean                                not null default true,
    is_deleted        boolean                                not null default false,
    deleted_by        bigint references users (id),
    deleted_at        timestamp with time zone
);

create index if not exists idx_resort_bookings_resort
    on resort_bookings (resort_id);

create unique index if not exists uq_resort_bookings_reference_code
    on resort_bookings (reference_code)
    where is_active = true and is_deleted = false;
