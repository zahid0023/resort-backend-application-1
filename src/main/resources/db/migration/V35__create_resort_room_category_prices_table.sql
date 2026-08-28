-- Resort room category pricing is split across two tables instead of one generic
-- (price_type, price)-keyed table:
--   resort_room_category_main_prices    -- BASE + WEEKDAY + WEEKEND, one row per (room category, currency)
--   resort_room_category_special_prices -- date-ranged SPECIAL rules (holidays, promotions, ...), each with its
--                                           own weekday/weekend price; `name`/`description` say what the rule is
--                                           for (e.g. "Eid-ul-Fitr", "Summer Promotion") — there is no separate
--                                           holiday concept, a holiday is just a special price with that name.
-- Pricing precedence when resolving a price for a given date is: SPECIAL -> WEEKDAY/WEEKEND -> BASE, with
-- weekday/weekend within any range decided by the resort's shared resort_weekly_schedule_days. Special rows for
-- the same room category/currency are allowed to have overlapping date ranges; `priority` (higher wins) is the
-- tie-breaker when more than one applies to the same date.

create table if not exists resort_room_category_main_prices
(
    id                      bigserial primary key,

    resort_room_category_id bigint references resort_room_categories (id) on delete cascade not null,

    -- PER_NIGHT / PER_DAY / PER_PERSON / ... — shared by base/weekday/weekend, this is "the whole set"'s unit.
    price_unit_id           bigint references price_units (id)                              not null,

    currency_id             bigint references currencies (id)                               not null,

    -- Default rack rate. The ceiling weekday_price/weekend_price cannot exceed.
    base_price              numeric(12, 2)                                                  not null,

    -- Normal weekday rate. Overrides base_price on weekday dates.
    weekday_price           numeric(12, 2)                                                  not null,

    -- Normal weekend rate. Overrides base_price on weekend dates.
    weekend_price           numeric(12, 2)                                                  not null,

    created_by              bigint references users (id)                                    not null,
    created_at              timestamp with time zone                                        not null default current_timestamp,
    updated_by              bigint references users (id)                                    not null,
    updated_at              timestamp with time zone                                        not null default current_timestamp,
    version                 bigint                                                          not null default 0,
    is_active               boolean                                                         not null default true,
    is_deleted              boolean                                                         not null default false,
    deleted_by              bigint references users (id),
    deleted_at              timestamp with time zone,

    constraint chk_resort_room_category_main_price_amounts
        check (base_price >= 0 and weekday_price >= 0 and weekend_price >= 0),

    constraint chk_resort_room_category_main_price_ceiling
        check (weekday_price <= base_price and weekend_price <= base_price)
);

-- At most one active main price row per (room category, currency) — the same physical-row guarantee
-- uq_resort_room_category_price_active_main gave the old table for its BAS rows.
create unique index if not exists uq_resort_room_category_main_price_active
    on resort_room_category_main_prices
        (
         resort_room_category_id,
         currency_id
            )
    where
        is_active = true
            and is_deleted = false;

create index if not exists idx_resort_room_category_main_prices_lookup
    on resort_room_category_main_prices (resort_room_category_id, is_active, is_deleted);

create index if not exists idx_resort_room_category_main_prices_currency
    on resort_room_category_main_prices (currency_id);

create table if not exists resort_room_category_special_prices
(
    id                      bigserial primary key,

    resort_room_category_id bigint references resort_room_categories (id) on delete cascade not null,

    price_unit_id           bigint references price_units (id)                              not null,

    currency_id             bigint references currencies (id)                               not null,

    -- Display name, e.g. "Summer Promotion", "New Year's Eve".
    name                    varchar(200)                                                    not null,

    description             text,

    -- Date range this promotion's pricing applies to.
    valid_from              date                                                            not null,
    valid_to                date                                                            not null,

    -- Special weekday rate — applies on weekday dates within [valid_from, valid_to]. No cap vs. base_price.
    weekday_price           numeric(12, 2)                                                  not null,

    -- Special weekend rate — applies on weekend dates within [valid_from, valid_to]. No cap vs. base_price.
    weekend_price           numeric(12, 2)                                                  not null,

    -- Tie-breaker when multiple HOLIDAY/SPECIAL rules could apply to the same date — higher wins.
    priority                integer                                                         not null default 0,

    created_by              bigint references users (id)                                    not null,
    created_at              timestamp with time zone                                        not null default current_timestamp,
    updated_by              bigint references users (id)                                    not null,
    updated_at              timestamp with time zone                                        not null default current_timestamp,
    version                 bigint                                                          not null default 0,
    is_active               boolean                                                         not null default true,
    is_deleted              boolean                                                         not null default false,
    deleted_by              bigint references users (id),
    deleted_at              timestamp with time zone,

    constraint chk_resort_room_category_special_price_amounts
        check (weekday_price >= 0 and weekend_price >= 0),

    constraint chk_resort_room_category_special_price_dates
        check (valid_from <= valid_to)
);

create index if not exists idx_resort_room_category_special_prices_lookup
    on resort_room_category_special_prices
        (
         resort_room_category_id,
         currency_id,
         is_active,
         is_deleted,
         priority desc
            );

create index if not exists idx_resort_room_category_special_prices_dates
    on resort_room_category_special_prices (valid_from, valid_to);

-- Enforces that a resort_room_category_{main,special}_prices row only ever references a
-- price_unit_id that is actually assigned to the ROOM_CATEGORY price scope (price_unit_scope_assignments /
-- V17). Without this, that assignment table is just unenforced metadata and a row could reference a price
-- unit meant for another scope (e.g. RESORT_FACILITY). Shared by both tables' triggers below.
create or replace function fn_validate_resort_room_category_price_unit_scope()
    returns trigger as
$$
begin
    if not exists (select 1
                   from price_unit_scope_assignments pusa
                            join price_scopes ps on ps.id = pusa.price_scope_id
                   where pusa.price_unit_id = new.price_unit_id
                     and ps.code = 'ROOM_CATEGORY'
                     and pusa.is_active = true
                     and pusa.is_deleted = false
                     and ps.is_active = true
                     and ps.is_deleted = false) then
        raise exception 'price_unit_id % is not assigned to the ROOM_CATEGORY price scope', new.price_unit_id;
    end if;

    return new;
end;
$$ language plpgsql;

create trigger trg_validate_resort_room_category_main_price_unit_scope
    before insert or update
    on resort_room_category_main_prices
    for each row
execute function fn_validate_resort_room_category_price_unit_scope();

create trigger trg_validate_resort_room_category_special_price_unit_scope
    before insert or update
    on resort_room_category_special_prices
    for each row
execute function fn_validate_resort_room_category_price_unit_scope();

-- Every row on both tables always carries both a weekday and a weekend price, so the owning resort must
-- already have at least one resort_weekly_schedule_days row for both WKD and WKE before an active row can
-- exist. Deferred so it runs at transaction commit — a resort's weekly schedule is expected to already exist
-- (set once via PUT /resorts/{resort-id}/weekly-schedule) before any of its room categories get an active
-- price, but deferring still allows a fresh resort's schedule and its first room category's prices to be
-- written in the same transaction if a caller chooses to. Shared by both tables' triggers below.
create or replace function fn_validate_resort_room_category_price_days_required()
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
        from resort_room_categories rc
        where rc.id = new.resort_room_category_id;

        select count(*) filter (where d.day_type = 'WEEKDAY'), count(*) filter (where d.day_type = 'WEEKEND')
        into v_wkd_count, v_wke_count
        from resort_weekly_schedule_days d
        where d.resort_id = v_resort_id
          and d.is_active = true
          and d.is_deleted = false;

        if v_wkd_count = 0 or v_wke_count = 0 then
            raise exception 'resort room category price % requires the resort to have at least one weekly schedule day for both WEEKDAY and WEEKEND — set one via PUT /resorts/%/weekly-schedule',
                new.id, v_resort_id;
        end if;
    end if;

    return new;
end;
$$ language plpgsql;

create constraint trigger trg_validate_resort_room_category_main_price_days_required
    after insert or update
    on resort_room_category_main_prices
    deferrable initially deferred
    for each row
execute function fn_validate_resort_room_category_price_days_required();

create constraint trigger trg_validate_resort_room_category_special_price_days_required
    after insert or update
    on resort_room_category_special_prices
    deferrable initially deferred
    for each row
execute function fn_validate_resort_room_category_price_days_required();
