-- Which days of week count as WEEKDAY/WEEKEND for a resort — shared by every room category at that
-- resort, not scoped per currency/price row. Which days are "weekend" is a property of the resort's
-- real-world location/calendar, not of which currency a guest happens to pay in, so this deliberately
-- lives at the resort level rather than being duplicated per (room category, currency) the way an
-- earlier version of this schema did (resort_room_category_price_days, FK'd to a specific price row —
-- removed here). A resort's schedule must exist before any of its room categories can have an active
-- WEEKDAY/WEEKEND price, see fn_validate_resort_room_category_price_days_required in
-- V35__create_resort_room_category_prices_table.sql.
create table if not exists resort_weekly_schedule_days
(
    id             bigserial primary key,

    resort_id      bigint references resorts (id) on delete cascade not null,

    -- WEEKDAY
    -- WEEKEND
    price_type_id  bigint references price_types (id)                not null,

    -- MONDAY
    -- TUESDAY
    -- WEDNESDAY
    -- THURSDAY
    -- FRIDAY
    -- SATURDAY
    -- SUNDAY
    day_of_week_id bigint references days_of_week (id)                not null,

    created_by     bigint references users (id)                      not null,
    created_at     timestamp with time zone                          not null default current_timestamp,
    updated_by     bigint references users (id)                      not null,
    updated_at     timestamp with time zone                          not null default current_timestamp,
    version        bigint                                            not null default 0,
    is_active      boolean                                           not null default true,
    is_deleted     boolean                                           not null default false,
    deleted_by     bigint references users (id),
    deleted_at     timestamp with time zone
);

-- Deliberately not a unique index: a "replace the whole schedule" write soft-deletes every existing
-- active row and inserts a fresh set in the same transaction, and none of (resort_id, price_type_id,
-- day_of_week_id) are nullable, so a unique index would collide with itself on the second replace.
-- Uniqueness within one submitted schedule is instead enforced at the application layer
-- (ResortWeeklyScheduleValidator), mirroring resort_facility_operating_hours' identical precedent.
create index if not exists idx_resort_weekly_schedule_days_lookup
    on resort_weekly_schedule_days (resort_id, price_type_id, day_of_week_id);

-- A resort's weekly schedule rows may only classify a day as WEEKDAY or WEEKEND.
create or replace function fn_validate_resort_weekly_schedule_day_type()
    returns trigger as
$$
declare
    v_price_type_code varchar(50);
begin
    select code
    into v_price_type_code
    from price_types
    where id = new.price_type_id;

    if v_price_type_code not in ('WKD', 'WKE') then
        raise exception 'resort weekly schedule days are only allowed for WEEKDAY/WEEKEND price types, not %', v_price_type_code;
    end if;

    return new;
end;
$$ language plpgsql;

create trigger trg_validate_resort_weekly_schedule_day_type
    before insert or update
    on resort_weekly_schedule_days
    for each row
execute function fn_validate_resort_weekly_schedule_day_type();
