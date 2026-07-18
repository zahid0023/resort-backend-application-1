create table if not exists resort_room_category_prices
(
    id                      bigserial primary key,

    -- Resort room category this pricing rule belongs to.
    resort_room_category_id bigint                                             not null references resort_room_categories (id) on delete cascade,

    -- Type of price.
    -- Examples:
    -- BASE
    -- PROMOTIONAL
    -- CORPORATE
    -- MEMBER
    price_type_id           bigint                                             not null references price_types (id),

    -- Charging unit.
    -- Examples:
    -- Per Night
    -- Per Day
    -- Per Person
    price_unit_id           bigint                                             not null references price_units (id),

    -- Price amount.
    amount                  numeric(12, 2)                                     not null,

    -- Rule priority.
    -- Higher priority wins when multiple rules match.
    priority                integer                  default 0                 not null,

    -- Rule validity period.
    -- NULL means no date restriction.
    valid_from              date,
    valid_to                date,

    -- Days of week this rule applies to.
    monday                  boolean                  default false             not null,
    tuesday                 boolean                  default false             not null,
    wednesday               boolean                  default false             not null,
    thursday                boolean                  default false             not null,
    friday                  boolean                  default false             not null,
    saturday                boolean                  default false             not null,
    sunday                  boolean                  default false             not null,

    created_by              bigint                                             not null references users (id),
    created_at              timestamp with time zone default current_timestamp not null,
    updated_by              bigint                                             not null references users (id),
    updated_at              timestamp with time zone default current_timestamp not null,
    version                 bigint                   default 0                 not null,
    is_active               boolean                  default true              not null,
    is_deleted              boolean                  default false             not null,
    deleted_by              bigint references users (id),
    deleted_at              timestamp with time zone,

    constraint chk_resort_room_category_price_amount
        check (amount >= 0),

    constraint chk_resort_room_category_price_dates
        check (
            valid_to is null
                or valid_from is null
                or valid_from <= valid_to
            ),

    constraint chk_resort_room_category_price_days
        check (
            monday
                or tuesday
                or wednesday
                or thursday
                or friday
                or saturday
                or sunday
            )
);

create index idx_resort_room_category_prices_category
    on resort_room_category_prices (resort_room_category_id);

create index idx_resort_room_category_prices_type
    on resort_room_category_prices (price_type_id);

create index idx_resort_room_category_prices_unit
    on resort_room_category_prices (price_unit_id);

create index idx_resort_room_category_prices_dates
    on resort_room_category_prices (valid_from, valid_to);

create index idx_resort_room_category_prices_priority
    on resort_room_category_prices (priority);