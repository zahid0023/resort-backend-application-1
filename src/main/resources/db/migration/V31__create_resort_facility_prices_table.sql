create table if not exists resort_facility_prices
(
    id                 bigserial primary key,

    -- Resort facility being priced.
    resort_facility_id bigint references resort_facilities (id) not null,

    -- Generic pricing classification.
    -- Examples:
    -- FREE
    -- INCLUDED
    -- FIXED
    -- VARIABLE
    price_type_id      bigint references price_types (id)       not null,

    -- Generic pricing unit.
    -- Examples:
    -- PER_PERSON
    -- PER_ROOM
    -- PER_NIGHT
    -- PER_DAY
    -- PER_HOUR
    -- PER_BOOKING
    --
    -- Nullable because FREE / INCLUDED may not require
    -- a billing unit.
    price_unit_id      bigint references price_units (id),

    currency_id        bigint references currencies (id)        not null,

    -- Monetary amount.
    -- Nullable for FREE / INCLUDED pricing types.
    amount             numeric(19, 4),
    note               text                                     not null default '',

    created_by         bigint references users (id)             not null,
    created_at         timestamp with time zone                 not null default current_timestamp,
    updated_by         bigint references users (id)             not null,
    updated_at         timestamp with time zone                 not null default current_timestamp,
    version            bigint                                   not null default 0,
    is_active          boolean                                  not null default true,
    is_deleted         boolean                                  not null default false,
    deleted_by         bigint references users (id),
    deleted_at         timestamp with time zone
);