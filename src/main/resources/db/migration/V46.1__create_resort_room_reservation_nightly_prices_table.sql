-- Frozen, per-night price snapshot for a resort room reservation — captures exactly what PricingCalculator
-- resolved for each night at booking time. resort_room_reservations.total_price (V46) is already frozen so
-- later changes to resort_room_prices/resort_room_category_prices don't retroactively change what an existing
-- guest agreed to pay; this table is the per-night breakdown behind that aggregate, so "what did night N of
-- this reservation actually cost" stays answerable even after prices change.
create type rate_type as enum ('WEEKDAY', 'WEEKEND', 'SPECIAL');

create table if not exists resort_room_reservation_nightly_prices
(
    id                         bigserial primary key,

    resort_room_reservation_id bigint references resort_room_reservations (id) on delete cascade not null,

    night_date                 date                                                              not null,

    rate_type                  rate_type                                                         not null,

    price                      numeric(12, 2)                                                    not null,

    created_by                 bigint references users (id)                                      not null,
    created_at                 timestamp with time zone                                          not null default current_timestamp,
    updated_by                 bigint references users (id)                                      not null,
    updated_at                 timestamp with time zone                                          not null default current_timestamp,
    version                    bigint                                                            not null default 0,
    is_active                  boolean                                                           not null default true,
    is_deleted                 boolean                                                           not null default false,
    deleted_by                 bigint references users (id),
    deleted_at                 timestamp with time zone,

    constraint chk_resort_room_reservation_nightly_prices_price
        check (price >= 0)
);

create unique index if not exists uq_resort_room_reservation_nightly_prices_reservation_date
    on resort_room_reservation_nightly_prices (resort_room_reservation_id, night_date)
    where is_active = true and is_deleted = false;

create index if not exists idx_resort_room_reservation_nightly_prices_reservation
    on resort_room_reservation_nightly_prices (resort_room_reservation_id);
