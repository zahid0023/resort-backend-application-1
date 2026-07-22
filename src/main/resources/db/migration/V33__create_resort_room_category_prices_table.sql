create table if not exists resort_room_category_prices
(
    id                      bigserial primary key,

    -- Resort room category this pricing rule belongs to.
    resort_room_category_id bigint                                             not null
        references resort_room_categories (id) on delete cascade,

    -- BASE
    -- WEEKDAY
    -- WEEKEND
    -- HOLIDAY
    -- SPECIAL
    price_type_id           bigint                                             not null
        references price_types (id),

    -- PER_NIGHT
    -- PER_DAY
    -- PER_PERSON
    price_unit_id           bigint                                             not null
        references price_units (id),

    -- Currency.
    currency_id             bigint                                             not null
        references currencies (id),

    -- Display name.
    -- Examples:
    -- Base Price
    -- Weekday Price
    -- Weekend Price
    -- Eid-ul-Fitr
    -- Christmas
    -- Summer Sale
    name                    varchar(200)                                       not null,

    -- Optional description.
    description             text,

    -- Price amount.
    price                   numeric(12, 2)                                     not null,

    -- Valid date range.
    -- Used mainly for HOLIDAY and SPECIAL pricing.
    valid_from              date,
    valid_to                date,

    -- Higher priority overrides lower priority.
    -- Example:
    -- BASE      = 0
    -- WEEKDAY   = 10
    -- WEEKEND   = 20
    -- HOLIDAY   = 100
    -- SPECIAL   = 200
    priority                integer                  default 0                 not null,

    created_by              bigint                                             not null
        references users (id),
    created_at              timestamp with time zone default current_timestamp not null,
    updated_by              bigint                                             not null
        references users (id),
    updated_at              timestamp with time zone default current_timestamp not null,
    version                 bigint                   default 0                 not null,
    is_active               boolean                  default true              not null,
    is_deleted              boolean                  default false             not null,
    deleted_by              bigint
        references users (id),
    deleted_at              timestamp with time zone,

    constraint chk_resort_room_category_price
        check (price >= 0),

    constraint chk_resort_room_category_price_dates
        check
            (
            valid_from is null
                or valid_to is null
                or valid_from <= valid_to
            )
);

create unique index uq_resort_room_category_price_rule
    on resort_room_category_prices
        (
         resort_room_category_id,
         price_type_id,
         currency_id,
         valid_from,
         valid_to,
         is_deleted
            );

create index idx_resort_room_category_prices_lookup
    on resort_room_category_prices
        (
         resort_room_category_id,
         is_active,
         is_deleted,
         priority desc
            );

create index idx_resort_room_category_prices_type
    on resort_room_category_prices (price_type_id);

create index idx_resort_room_category_prices_dates
    on resort_room_category_prices (valid_from, valid_to);

create index idx_resort_room_category_prices_currency
    on resort_room_category_prices (currency_id);

create table if not exists resort_room_category_price_days
(
    id                            bigserial primary key,

    -- Pricing rule.
    resort_room_category_price_id bigint                                             not null
        references resort_room_category_prices (id)
            on delete cascade,

    -- MONDAY
    -- TUESDAY
    -- WEDNESDAY
    -- THURSDAY
    -- FRIDAY
    -- SATURDAY
    -- SUNDAY
    day_of_week_id                bigint                                             not null
        references days_of_week (id),

    created_by                    bigint                                             not null
        references users (id),
    created_at                    timestamp with time zone default current_timestamp not null,
    updated_by                    bigint                                             not null
        references users (id),
    updated_at                    timestamp with time zone default current_timestamp not null,
    version                       bigint                   default 0                 not null,
    is_active                     boolean                  default true              not null,
    is_deleted                    boolean                  default false             not null,
    deleted_by                    bigint
        references users (id),
    deleted_at                    timestamp with time zone,

    constraint uq_resort_room_category_price_day
        unique
            (
             resort_room_category_price_id,
             day_of_week_id
                )
);

create index idx_resort_room_category_price_days_price
    on resort_room_category_price_days (resort_room_category_price_id);

create index idx_resort_room_category_price_days_day
    on resort_room_category_price_days (day_of_week_id);