create table if not exists resort_facility_operating_hours
(
    id                   bigserial primary key,

    resort_facility_id   bigint references resort_facilities (id) not null,
    day_of_week_id       bigint references days_of_week (id)      not null,

    opens_at             time,
    closes_at            time,

    is_closed            boolean                                  not null default false,
    is_twenty_four_hours boolean                                  not null default false,

    created_by           bigint references users (id)             not null,
    created_at           timestamp with time zone                 not null default current_timestamp,
    updated_by           bigint references users (id)             not null,
    updated_at           timestamp with time zone                 not null default current_timestamp,
    version              bigint                                   not null default 0,
    is_active            boolean                                  not null default true,
    is_deleted           boolean                                  not null default false,
    deleted_by           bigint references users (id),
    deleted_at           timestamp with time zone,

    constraint chk_facility_operating_hours
        check (
            (is_closed = true and is_twenty_four_hours = false and opens_at is null and closes_at is null)
                or
            (is_closed = false and is_twenty_four_hours = true and opens_at is null and closes_at is null)
                or
            (is_closed = false and is_twenty_four_hours = false and opens_at is not null and closes_at is not null)
            )
);

create index if not exists idx_resort_facility_operating_hours_facility_day
    on resort_facility_operating_hours (resort_facility_id, day_of_week_id);
