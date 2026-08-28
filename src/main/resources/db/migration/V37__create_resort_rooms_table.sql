create table if not exists resort_rooms
(
    id                      bigserial primary key,

    resort_room_category_id bigint references resort_room_categories (id) not null,
    room_status_id          bigint references room_statuses (id)          not null,

    -- resort-scoped identifier, unique per resort. The resort itself is reached via
    -- resort_room_category_id -> resort_room_categories.resort_id — not duplicated as its own column here, so
    -- the uniqueness below is enforced by trigger (see fn_validate_resort_room_code_unique_per_resort) instead
    -- of a plain index, since a plain index can't reach through that join.
    code                    varchar(50)                                   not null,
    sort_order              integer                                       not null default 0,

    -- physical location, both optional — free-form (not a fixed vocabulary, so no lookup table).
    floor_number            integer,
    building                varchar(100),

    created_by              bigint references users (id)                  not null,
    created_at              timestamp with time zone                      not null default current_timestamp,
    updated_by              bigint references users (id)                  not null,
    updated_at              timestamp with time zone                      not null default current_timestamp,
    version                 bigint                                        not null default 0,
    is_active               boolean                                       not null default true,
    is_deleted              boolean                                       not null default false,
    deleted_by              bigint references users (id),
    deleted_at              timestamp with time zone
);

create or replace function fn_validate_resort_room_code_unique_per_resort()
    returns trigger as
$$
declare
    v_resort_id bigint;
begin
    if new.is_active = true and new.is_deleted = false then
        select resort_id
        into v_resort_id
        from resort_room_categories
        where id = new.resort_room_category_id;

        if exists (select 1
                   from resort_rooms rr
                            join resort_room_categories rc on rc.id = rr.resort_room_category_id
                   where rc.resort_id = v_resort_id
                     and rr.code = new.code
                     and rr.id <> new.id
                     and rr.is_active = true
                     and rr.is_deleted = false) then
            raise exception 'code % is already used by another room in resort %', new.code, v_resort_id;
        end if;
    end if;

    return new;
end;
$$ language plpgsql;

create trigger trg_validate_resort_room_code_unique_per_resort
    before insert or update
    on resort_rooms
    for each row
execute function fn_validate_resort_room_code_unique_per_resort();

create table if not exists resort_room_locales
(
    id             bigserial primary key,

    resort_room_id bigint references resort_rooms (id) not null,
    locale_id      bigint references locales (id)      not null,

    name           varchar(150)                        not null,
    description    text                                not null default '',
    sort_order     integer                             not null default 0,

    created_by     bigint references users (id)        not null,
    created_at     timestamp with time zone            not null default current_timestamp,
    updated_by     bigint references users (id)        not null,
    updated_at     timestamp with time zone            not null default current_timestamp,
    version        bigint                              not null default 0,
    is_active      boolean                             not null default true,
    is_deleted     boolean                             not null default false,
    deleted_by     bigint references users (id),
    deleted_at     timestamp with time zone
);

create unique index if not exists uq_resort_room_locale
    on resort_room_locales (resort_room_id, locale_id)
    where is_active = true and is_deleted = false;

-- A resort_room inherits its meta from resort_room_category_metas by default. A row here for a given
-- resort_room_id is a full override of that room's own meta; no row means "inherit from the category".
create table if not exists resort_room_metas
(
    id                  bigserial primary key,

    resort_room_id      bigint references resort_rooms (id) not null,

    -- occupancy
    max_adults          integer                             not null default 2,
    max_children        integer                             not null default 0,
    max_infants         integer                             not null default 0,
    max_occupancy       integer                             not null default 2,

    -- room details
    room_size           numeric(10, 2),
    room_size_unit_id   bigint references units (id),
    bedroom_count       integer                             not null default 1,
    bathroom_count      integer                             not null default 1,

    -- booking rules
    minimum_stay_nights integer                             not null default 1,
    maximum_stay_nights integer,

    created_by          bigint references users (id)        not null,
    created_at          timestamp with time zone            not null default current_timestamp,
    updated_by          bigint references users (id)        not null,
    updated_at          timestamp with time zone            not null default current_timestamp,
    version             bigint                              not null default 0,
    is_active           boolean                             not null default true,
    is_deleted          boolean                             not null default false,
    deleted_by          bigint references users (id),
    deleted_at          timestamp with time zone,

    constraint chk_resort_room_meta_occupancy
        check (max_occupancy >= max_adults + max_children + max_infants),
    constraint chk_resort_room_meta_room_size
        check (room_size is null or room_size > 0),
    constraint chk_resort_room_meta_bedroom_count
        check (bedroom_count > 0),
    constraint chk_resort_room_meta_bathroom_count
        check (bathroom_count > 0),
    constraint chk_resort_room_meta_minimum_stay
        check (minimum_stay_nights > 0),
    constraint chk_resort_room_meta_maximum_stay
        check (maximum_stay_nights is null or maximum_stay_nights >= minimum_stay_nights)
);

create unique index if not exists uq_resort_room_meta
    on resort_room_metas (resort_room_id)
    where is_active = true and is_deleted = false;

-- A resort_room inherits its bed list from resort_room_category_beds by default. Any rows here for a given
-- resort_room_id are a full override of that room's own bed list; no rows means "inherit from the category".
create table if not exists resort_room_beds
(
    id                   bigserial primary key,

    resort_room_id       bigint references resort_rooms (id) not null,
    bed_type_id          bigint references bed_types (id)    not null,

    quantity             integer                             not null default 1,

    -- extra bed
    is_extra_bed_allowed boolean                             not null default false,
    max_extra_beds       integer                             not null default 0,

    created_by           bigint references users (id)        not null,
    created_at           timestamp with time zone            not null default current_timestamp,
    updated_by           bigint references users (id)        not null,
    updated_at           timestamp with time zone            not null default current_timestamp,
    version              bigint                              not null default 0,
    is_active            boolean                             not null default true,
    is_deleted           boolean                             not null default false,
    deleted_by           bigint references users (id),
    deleted_at           timestamp with time zone,

    constraint chk_resort_room_bed_quantity
        check (quantity > 0),
    constraint chk_resort_room_bed_extra_beds
        check (max_extra_beds >= 0)
);

create unique index if not exists uq_resort_room_bed
    on resort_room_beds (resort_room_id, bed_type_id)
    where is_active = true and is_deleted = false;
