create table if not exists resort_room_facilities
(
    id                          bigserial primary key,

    resort_room_id              bigint references resort_rooms (id)               not null,

    -- resort room facility group this facility belongs to.
    resort_room_facility_group_id bigint references resort_room_facility_groups (id) not null,

    -- optional link to a platform-defined facility.
    -- null means this is a resort-defined custom facility.
    facility_id                 bigint references facilities (id),

    -- resort-room-scoped identifier, unique per resort room.
    code                        varchar(100)                                       not null,
    -- display order in admin ui.
    sort_order                  integer                                            not null default 1,

    -- marks the facility as featured/promoted.
    is_highlighted               boolean                                            not null default false,

    -- optional override for the platform icon.
    -- for custom facilities, these are the actual icon values.

    icon_type                   varchar(100),
    icon_value                  text,
    icon_meta                   jsonb,

    created_by                  bigint references users (id)                      not null,
    created_at                  timestamp with time zone                          not null default current_timestamp,
    updated_by                  bigint references users (id)                      not null,
    updated_at                  timestamp with time zone                          not null default current_timestamp,
    version                     bigint                                             not null default 0,
    is_active                   boolean                                            not null default true,
    is_deleted                  boolean                                            not null default false,
    deleted_by                  bigint references users (id),
    deleted_at                  timestamp with time zone
);

-- resort-room-scoped code must be unique within a resort room.
create unique index if not exists uq_resort_room_facility_code
    on resort_room_facilities (resort_room_id, code)
    where is_active = true and is_deleted = false;

create table if not exists resort_room_facility_locales
(
    id                       bigserial primary key,

    resort_room_facility_id bigint references resort_room_facilities (id) not null,
    locale_id                bigint references locales (id)                not null,

    -- if linked to a platform facility, these override the defaults.
    -- if this is a custom facility, these are the actual values.
    name                     varchar(255)                                  not null,
    description              text                                         not null default '',
    -- free-form notes about this facility (e.g. internal remarks, guest-facing caveats).
    notes                    text                                         not null default '',
    sort_order               integer                                      not null default 1,

    created_by               bigint references users (id)                 not null,
    created_at               timestamp with time zone                     not null default current_timestamp,
    updated_by               bigint references users (id)                 not null,
    updated_at               timestamp with time zone                     not null default current_timestamp,
    version                  bigint                                       not null default 0,
    is_active                boolean                                      not null default true,
    is_deleted               boolean                                      not null default false,
    deleted_by               bigint references users (id),
    deleted_at               timestamp with time zone
);

create unique index if not exists uq_resort_room_facility_locale
    on resort_room_facility_locales (resort_room_facility_id, locale_id)
    where is_active = true and is_deleted = false;
