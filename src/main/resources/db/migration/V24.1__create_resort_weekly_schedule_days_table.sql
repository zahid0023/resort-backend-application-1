-- Which days of week count as WEEKDAY/WEEKEND for a resort — shared by every room category at that
-- resort, not scoped per currency/price row. Which days are "weekend" is a property of the resort's
-- real-world location/calendar, not of which currency a guest happens to pay in, so this deliberately
-- lives at the resort level rather than being duplicated per (room category, currency) the way an
-- earlier version of this schema did (resort_room_category_price_days, FK'd to a specific price row —
-- removed here). A resort's schedule must exist before any of its room categories can have an active
-- WEEKDAY/WEEKEND price, see fn_validate_resort_room_category_price_days_required in
-- V35__create_resort_room_category_prices_table.sql.
--
-- day_type is a native Postgres enum, not a price_types FK: WEEKDAY/WEEKEND is a fixed classification of
-- a calendar day, not a kind of price — reusing price_types' WKD/WKE codes here (as an earlier cut did) just
-- coupled this table to an unrelated pricing lookup table for no benefit, since this value is never
-- translated or shown to a client (see ResortWeeklyScheduleDayDto, which embeds only day_of_week).
create type day_type as enum ('WEEKDAY', 'WEEKEND');

create table if not exists resort_weekly_schedule_days
(
    id             bigserial primary key,

    resort_id      bigint references resorts (id) on delete cascade not null,

    day_type       day_type                                         not null,

    -- MONDAY
    -- TUESDAY
    -- WEDNESDAY
    -- THURSDAY
    -- FRIDAY
    -- SATURDAY
    -- SUNDAY
    day_of_week_id bigint references days_of_week (id)              not null,

    created_by     bigint references users (id)                     not null,
    created_at     timestamp with time zone                         not null default current_timestamp,
    updated_by     bigint references users (id)                     not null,
    updated_at     timestamp with time zone                         not null default current_timestamp,
    version        bigint                                           not null default 0,
    is_active      boolean                                          not null default true,
    is_deleted     boolean                                          not null default false,
    deleted_by     bigint references users (id),
    deleted_at     timestamp with time zone
);

-- Deliberately not a unique index: a "replace the whole schedule" write soft-deletes every existing
-- active row and inserts a fresh set in the same transaction, and none of (resort_id, day_type,
-- day_of_week_id) are nullable, so a unique index would collide with itself on the second replace.
-- Uniqueness within one submitted schedule is instead enforced at the application layer
-- (ResortWeeklyScheduleValidator), mirroring resort_facility_operating_hours' identical precedent.
create index if not exists idx_resort_weekly_schedule_days_lookup
    on resort_weekly_schedule_days (resort_id, day_type, day_of_week_id);
