create table if not exists resort_room_category_prices
(
    id                      bigserial primary key,

    -- Resort room category this pricing rule belongs to.
    resort_room_category_id bigint references resort_room_categories (id) on delete cascade not null,

    -- BASE
    -- WEEKDAY
    -- WEEKEND
    -- HOLIDAY
    -- SPECIAL
    price_type_id           bigint references price_types (id)                              not null,

    -- PER_NIGHT
    -- PER_DAY
    -- PER_PERSON
    price_unit_id           bigint references price_units (id)                              not null,

    -- Currency.
    currency_id             bigint references currencies (id)                               not null,

    -- Display name.
    -- Examples:
    -- Base Price
    -- Weekday Price
    -- Weekend Price
    -- Eid-ul-Fitr
    -- Christmas
    -- Summer Sale
    name                    varchar(200)                                                    not null,

    -- Optional description.
    description             text,

    -- Price amount.
    price                   numeric(12, 2)                                                  not null,

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

create unique index if not exists uq_resort_room_category_price_rule
    on resort_room_category_prices
        (
         resort_room_category_id,
         price_type_id,
         currency_id,
         valid_from,
         valid_to,
         is_deleted
            );

-- uq_resort_room_category_price_rule above does not actually protect BASE/WEEKDAY/WEEKEND
-- rows: their valid_from/valid_to are always NULL, and Postgres treats NULL as distinct from
-- NULL in unique indexes, so two active rows with identical
-- (resort_room_category_id, price_type_id, currency_id, NULL, NULL, false) do not collide there.
-- fn_validate_resort_room_category_price_type_rules' duplicate check below is a plain SELECT
-- with no locking, so two concurrent inserts for the same room category/currency can both pass
-- it before either commits. This index has no NULL columns in its key, so Postgres enforces it
-- atomically at insert time regardless of concurrency.
create unique index if not exists uq_resort_room_category_price_active_main
    on resort_room_category_prices
        (
         resort_room_category_id,
         price_type_id,
         currency_id
            )
    where
        valid_from is null
            and valid_to is null
            and is_active = true
            and is_deleted = false;

create index if not exists idx_resort_room_category_prices_lookup
    on resort_room_category_prices
        (
         resort_room_category_id,
         is_active,
         is_deleted,
         priority desc
            );

create index if not exists idx_resort_room_category_prices_type
    on resort_room_category_prices (price_type_id);

create index if not exists idx_resort_room_category_prices_dates
    on resort_room_category_prices (valid_from, valid_to);

create index if not exists idx_resort_room_category_prices_currency
    on resort_room_category_prices (currency_id);

-- Enforces that resort_room_category_prices only ever references a
-- price_type_id / price_unit_id that is actually assigned to the
-- ROOM_CATEGORY price scope (price_type_scope_assignments / V16,
-- price_unit_scope_assignments / V17). Without this, those assignment
-- tables are just unenforced metadata and a row could reference a
-- price type/unit meant for another scope (e.g. RESORT_FACILITY).
create or replace function fn_validate_resort_room_category_price_scope()
    returns trigger as
$$
begin
    if not exists (select 1
                   from price_type_scope_assignments ptsa
                            join price_scopes ps on ps.id = ptsa.price_scope_id
                   where ptsa.price_type_id = new.price_type_id
                     and ps.code = 'ROOM_CATEGORY'
                     and ptsa.is_active = true
                     and ptsa.is_deleted = false
                     and ps.is_active = true
                     and ps.is_deleted = false) then
        raise exception 'price_type_id % is not assigned to the ROOM_CATEGORY price scope', new.price_type_id;
    end if;

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

create trigger trg_validate_resort_room_category_price_scope
    before insert or update
    on resort_room_category_prices
    for each row
execute function fn_validate_resort_room_category_price_scope();

-- Enforces the day-records/date-range shape per price type:
-- BASE               -> neither a date range nor day records.
-- WEEKDAY / WEEKEND   -> no date range (day records are checked separately,
--                        see trg_validate_resort_room_category_price_days_required).
-- HOLIDAY / SPECIAL   -> a date range is required.
-- Also forces priority to a fixed value for BASE/WEEKDAY/WEEKEND, since owners
-- are not allowed to edit it for those types, and rejects a WEEKDAY/WEEKEND
-- price that exceeds the room category's active BASE price in the same
-- currency. This only runs at write time, so an existing WEEKDAY/WEEKEND row
-- is not re-checked if the BASE price is lowered afterward.
-- BASE/WEEKDAY/WEEKEND are also limited to a single active/non-deleted row per
-- room category/currency here (an active-row uniqueness check, not a plain
-- unique index, since it only applies to these three types — HOLIDAY/SPECIAL
-- are date-ranged rules and a room category/currency may legitimately have
-- many active HOLIDAY/SPECIAL rows at once).
create or replace function fn_validate_resort_room_category_price_type_rules()
    returns trigger as
$$
declare
    v_price_type_code varchar(50);
    v_base_price      numeric(12, 2);
    v_duplicate_id     bigint;
begin
    select code
    into v_price_type_code
    from price_types
    where id = new.price_type_id;

    if v_price_type_code in ('BAS', 'WKD', 'WKE') then
        if new.valid_from is not null or new.valid_to is not null then
            raise exception '% prices do not support a valid_from/valid_to date range', v_price_type_code;
        end if;

        new.priority := 0;

        if new.is_active = true and new.is_deleted = false then
            select p.id
            into v_duplicate_id
            from resort_room_category_prices p
            where p.resort_room_category_id = new.resort_room_category_id
              and p.price_type_id = new.price_type_id
              and p.currency_id = new.currency_id
              and p.is_active = true
              and p.is_deleted = false
              and p.id <> coalesce(new.id, -1)
            limit 1;

            if v_duplicate_id is not null then
                raise exception 'This room category already has an active % price for currency id: %',
                    v_price_type_code, new.currency_id;
            end if;
        end if;
    end if;

    if v_price_type_code in ('HOL', 'SPECIAL') then
        if new.valid_from is null or new.valid_to is null then
            raise exception '% prices require both valid_from and valid_to', v_price_type_code;
        end if;
    end if;

    if v_price_type_code in ('WKD', 'WKE') then
        select p.price
        into v_base_price
        from resort_room_category_prices p
                 join price_types pt on pt.id = p.price_type_id
        where p.resort_room_category_id = new.resort_room_category_id
          and p.currency_id = new.currency_id
          and pt.code = 'BAS'
          and p.is_active = true
          and p.is_deleted = false
          and p.id <> coalesce(new.id, -1)
        limit 1;

        if v_base_price is not null and new.price > v_base_price then
            raise exception '% price (%) cannot exceed the BASE price (%) for this room category/currency',
                v_price_type_code, new.price, v_base_price;
        end if;
    end if;

    return new;
end;
$$ language plpgsql;

create trigger trg_validate_resort_room_category_price_type_rules
    before insert or update
    on resort_room_category_prices
    for each row
execute function fn_validate_resort_room_category_price_type_rules();

-- Deferred so it runs at transaction commit: a resort's weekly schedule is expected to already exist
-- (set once via PUT /resorts/{resort-id}/weekly-schedule) before any of its room categories get an
-- active WEEKDAY/WEEKEND price, but deferring still allows a fresh resort's schedule and its first room
-- category's prices to be written in the same transaction if a caller chooses to. Joins through
-- resort_room_categories to resorts since resort_room_category_prices only carries
-- resort_room_category_id, not resort_id directly.
create or replace function fn_validate_resort_room_category_price_days_required()
    returns trigger as
$$
declare
    v_price_type_code varchar(50);
    v_resort_id        bigint;
    v_day_count        integer;
begin
    select code
    into v_price_type_code
    from price_types
    where id = new.price_type_id;

    if v_price_type_code in ('WKD', 'WKE') and new.is_active = true and new.is_deleted = false then
        select rc.resort_id
        into v_resort_id
        from resort_room_categories rc
        where rc.id = new.resort_room_category_id;

        select count(*)
        into v_day_count
        from resort_weekly_schedule_days
        where resort_id = v_resort_id
          and price_type_id = new.price_type_id
          and is_active = true
          and is_deleted = false;

        if v_day_count = 0 then
            raise exception '% price % requires the resort to have at least one weekly schedule day for that price type — set one via PUT /resorts/%/weekly-schedule',
                v_price_type_code, new.id, v_resort_id;
        end if;
    end if;

    return new;
end;
$$ language plpgsql;

create constraint trigger trg_validate_resort_room_category_price_days_required
    after insert or update
    on resort_room_category_prices
    deferrable initially deferred
    for each row
execute function fn_validate_resort_room_category_price_days_required();
