-- Resort room pricing mirrors resort room category pricing (V35), split the same way across two tables,
-- but every row here is an OPTIONAL, per-currency OVERRIDE of the owning resort room category's price, not an
-- independently required price. A resort room only gets a row in resort_room_main_prices for a currency it
-- wants to price differently than its category; no row for a given (resort_room_id, currency_id) means that
-- currency's price is inherited wholesale from the room's resort_room_category_{main,special}_prices (main +
-- special together, as one bundle) — resolved by the application layer, not by any FK/view here. Special
-- override rows for a room require that room to already have its own active main price for that currency (same
-- rule V35's category tables enforce), so a room can never have an "orphaned" special override with no main
-- override to anchor it.
create table if not exists resort_room_main_prices
(
    id             bigserial primary key,

    resort_room_id bigint references resort_rooms (id) on delete cascade not null,

    price_unit_id  bigint references price_units (id)                    not null,

    currency_id    bigint references currencies (id)                     not null,

    base_price     numeric(12, 2)                                        not null,

    weekday_price  numeric(12, 2)                                        not null,

    weekend_price  numeric(12, 2)                                        not null,

    created_by     bigint references users (id)                          not null,
    created_at     timestamp with time zone                              not null default current_timestamp,
    updated_by     bigint references users (id)                          not null,
    updated_at     timestamp with time zone                              not null default current_timestamp,
    version        bigint                                                not null default 0,
    is_active      boolean                                               not null default true,
    is_deleted     boolean                                               not null default false,
    deleted_by     bigint references users (id),
    deleted_at     timestamp with time zone,

    constraint chk_resort_room_main_price_amounts
        check (base_price >= 0 and weekday_price >= 0 and weekend_price >= 0),

    constraint chk_resort_room_main_price_ceiling
        check (weekday_price <= base_price and weekend_price <= base_price)
);

-- At most one active override row per (resort room, currency) — same physical-row guarantee
-- uq_resort_room_category_main_price_active gives the category level.
create unique index if not exists uq_resort_room_main_price_active
    on resort_room_main_prices
        (
         resort_room_id,
         currency_id
            )
    where
        is_active = true
            and is_deleted = false;

create index if not exists idx_resort_room_main_prices_lookup
    on resort_room_main_prices (resort_room_id, is_active, is_deleted);

create index if not exists idx_resort_room_main_prices_currency
    on resort_room_main_prices (currency_id);

create table if not exists resort_room_special_prices
(
    id             bigserial primary key,

    resort_room_id bigint references resort_rooms (id) on delete cascade not null,

    price_unit_id  bigint references price_units (id)                    not null,

    currency_id    bigint references currencies (id)                     not null,

    name           varchar(200)                                          not null,

    description    text,

    valid_from     date                                                  not null,
    valid_to       date                                                  not null,

    weekday_price  numeric(12, 2)                                        not null,

    weekend_price  numeric(12, 2)                                        not null,

    priority       integer                                               not null default 0,

    created_by     bigint references users (id)                          not null,
    created_at     timestamp with time zone                              not null default current_timestamp,
    updated_by     bigint references users (id)                          not null,
    updated_at     timestamp with time zone                              not null default current_timestamp,
    version        bigint                                                not null default 0,
    is_active      boolean                                               not null default true,
    is_deleted     boolean                                               not null default false,
    deleted_by     bigint references users (id),
    deleted_at     timestamp with time zone,

    constraint chk_resort_room_special_price_amounts
        check (weekday_price >= 0 and weekend_price >= 0),

    constraint chk_resort_room_special_price_dates
        check (valid_from <= valid_to)
);

create index if not exists idx_resort_room_special_prices_lookup
    on resort_room_special_prices
        (
         resort_room_id,
         currency_id,
         is_active,
         is_deleted,
         priority desc
            );

create index if not exists idx_resort_room_special_prices_dates
    on resort_room_special_prices (valid_from, valid_to);

-- Same shape as fn_validate_resort_room_category_price_unit_scope (V35), but checks the ROOM price scope
-- (distinct from ROOM_CATEGORY — both already seeded by V15) since a resort room price is a different scope
-- of price than its category's. Shared by both tables' triggers below.
create or replace function fn_validate_resort_room_price_unit_scope()
    returns trigger as
$$
begin
    if not exists (select 1
                   from price_unit_scope_assignments pusa
                            join price_scopes ps on ps.id = pusa.price_scope_id
                   where pusa.price_unit_id = new.price_unit_id
                     and ps.code = 'ROOM'
                     and pusa.is_active = true
                     and pusa.is_deleted = false
                     and ps.is_active = true
                     and ps.is_deleted = false) then
        raise exception 'price_unit_id % is not assigned to the ROOM price scope', new.price_unit_id;
    end if;

    return new;
end;
$$ language plpgsql;

create trigger trg_validate_resort_room_main_price_unit_scope
    before insert or update
    on resort_room_main_prices
    for each row
execute function fn_validate_resort_room_price_unit_scope();

create trigger trg_validate_resort_room_special_price_unit_scope
    before insert or update
    on resort_room_special_prices
    for each row
execute function fn_validate_resort_room_price_unit_scope();

-- Same shape as fn_validate_resort_room_category_price_days_required (V35), reached through
-- resort_rooms -> resort_room_categories -> resort_id instead of directly. Shared by both tables'
-- triggers below.
create or replace function fn_validate_resort_room_price_days_required()
    returns trigger as
$$
declare
    v_resort_id bigint;
    v_wkd_count integer;
    v_wke_count integer;
begin
    if new.is_active = true and new.is_deleted = false then
        select rc.resort_id
        into v_resort_id
        from resort_rooms rr
                 join resort_room_categories rc on rc.id = rr.resort_room_category_id
        where rr.id = new.resort_room_id;

        select count(*) filter (where d.day_type = 'WEEKDAY'), count(*) filter (where d.day_type = 'WEEKEND')
        into v_wkd_count, v_wke_count
        from resort_weekly_schedule_days d
        where d.resort_id = v_resort_id
          and d.is_active = true
          and d.is_deleted = false;

        if v_wkd_count = 0 or v_wke_count = 0 then
            raise exception 'resort room price % requires the resort to have at least one weekly schedule day for both WEEKDAY and WEEKEND — set one via PUT /resorts/%/weekly-schedule',
                new.id, v_resort_id;
        end if;
    end if;

    return new;
end;
$$ language plpgsql;

create constraint trigger trg_validate_resort_room_main_price_days_required
    after insert or update
    on resort_room_main_prices
    deferrable initially deferred
    for each row
execute function fn_validate_resort_room_price_days_required();

create constraint trigger trg_validate_resort_room_special_price_days_required
    after insert or update
    on resort_room_special_prices
    deferrable initially deferred
    for each row
execute function fn_validate_resort_room_price_days_required();
