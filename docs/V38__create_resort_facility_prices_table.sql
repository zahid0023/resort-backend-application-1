create table if not exists resort_facility_prices
(
    id                 bigserial primary key,

    -- Resort facility this price belongs to.
    resort_facility_id bigint                   not null
        references resort_facilities (id)
            on delete cascade,

    -- Billing unit.
    -- Examples:
    -- PER_PERSON
    -- PER_HOUR
    -- PER_DAY
    -- PER_NIGHT
    -- PER_BOOKING
    -- PER_TRIP
    price_unit_id      bigint                   not null
        references price_units (id),

    -- Currency.
    currency_id        bigint                   not null
        references currencies (id),

    -- Price amount.
    amount             numeric(12, 2)           not null
        check (amount >= 0),

    -- Optional notes shown to guests or administrators.
    notes              text                     not null default '',

    -- Display order when a facility has multiple pricing options.
    sort_order         integer                  not null default 1,

    created_by         bigint                   not null
        references users (id),
    created_at         timestamp with time zone not null default current_timestamp,
    updated_by         bigint                   not null
        references users (id),
    updated_at         timestamp with time zone not null default current_timestamp,
    version            bigint                   not null default 0,
    is_active          boolean                  not null default true,
    is_deleted         boolean                  not null default false,
    deleted_by         bigint references users (id),
    deleted_at         timestamp with time zone
);

create index if not exists idx_resort_facility_prices_facility
    on resort_facility_prices (resort_facility_id);

create index if not exists idx_resort_facility_prices_price_unit
    on resort_facility_prices (price_unit_id);

create index if not exists idx_resort_facility_prices_currency
    on resort_facility_prices (currency_id);

create table if not exists facility_constraint_types
(
    id                bigserial primary key,

    facility_scope_id bigint references facility_scopes (id) not null,

    code              varchar(100)                           not null,

    -- Defines the primitive/container type of the constraint value.
    -- Examples: BOOLEAN, INTEGER, DECIMAL, STRING, ENUM, OBJECT, ARRAY
    value_type        varchar(50)                            not null,

    -- Defines the expected structure/validation rules for the value.
    value_schema      jsonb,

    sort_order        integer                                not null default 0,

    created_by        bigint references users (id)           not null,
    created_at        timestamp with time zone               not null default current_timestamp,
    updated_by        bigint references users (id)           not null,
    updated_at        timestamp with time zone               not null default current_timestamp,
    version           bigint                                 not null default 0,
    is_active         boolean                                not null default true,
    is_deleted        boolean                                not null default false,
    deleted_by        bigint references users (id),
    deleted_at        timestamp with time zone,

    constraint uq_facility_constraint_type_scope_code
        unique (facility_scope_id, code)
);

create table if not exists facility_constraint_type_locales
(
    id          bigserial primary key,

    facility_constraint_type_id
                bigint references facility_constraint_types (id) not null,

    locale_id   bigint references locales (id)                   not null,

    name        varchar(255)                                     not null,
    description text                                             not null default '',

    created_by  bigint references users (id)                     not null,
    created_at  timestamp with time zone                         not null default current_timestamp,
    updated_by  bigint references users (id)                     not null,
    updated_at  timestamp with time zone                         not null default current_timestamp,
    version     bigint                                           not null default 0,
    is_active   boolean                                          not null default true,
    is_deleted  boolean                                          not null default false,
    deleted_by  bigint references users (id),
    deleted_at  timestamp with time zone,

    constraint uq_facility_constraint_type_locale
        unique (facility_constraint_type_id, locale_id)
);