create table if not exists resort_facility_groups
(
    id                bigserial primary key,

    resort_id         bigint references resorts (id) not null,

    -- optional link to a platform-defined facility group.
    -- null means this is a resort-defined custom group.
    facility_group_id bigint references facility_groups (id),

    -- resort-scoped identifier, unique per resort.
    code              varchar(100)                   not null,
    -- display order in admin ui.
    sort_order        integer                        not null default 1,

    -- optional override for the platform icon.
    -- for custom groups, these are the actual icon values.
    icon_type         varchar(100),
    icon_value        text,
    icon_meta         jsonb,

    created_by        bigint references users (id)   not null,
    created_at        timestamp with time zone       not null default current_timestamp,
    updated_by        bigint references users (id)   not null,
    updated_at        timestamp with time zone       not null default current_timestamp,
    version           bigint                         not null default 0,
    is_active         boolean                        not null default true,
    is_deleted        boolean                        not null default false,
    deleted_by        bigint references users (id),
    deleted_at        timestamp with time zone
);

-- one resort can map to the same platform facility group at most once.
-- custom groups (facility_group_id is null) are excluded from this constraint.
create unique index if not exists uq_resort_facility_group_platform
    on resort_facility_groups (resort_id, facility_group_id)
    where facility_group_id is not null and is_active = true and is_deleted = false;

-- resort-scoped code must be unique within a resort.
create unique index if not exists uq_resort_facility_group_code
    on resort_facility_groups (resort_id, code)
    where is_active = true and is_deleted = false;

create table if not exists resort_facility_group_locales
(
    id                       bigserial primary key,

    resort_facility_group_id bigint references resort_facility_groups (id) not null,
    locale_id                bigint references locales (id)                not null,

    -- if linked to a platform group, these override the defaults.
    -- if this is a custom group, these are the actual values.
    name                     varchar(255)                                  not null,
    description              text                                          not null default '',
    sort_order               integer                                       not null default 1,

    created_by               bigint references users (id)                  not null,
    created_at               timestamp with time zone                      not null default current_timestamp,
    updated_by               bigint references users (id)                  not null,
    updated_at               timestamp with time zone                      not null default current_timestamp,
    version                  bigint                                        not null default 0,
    is_active                boolean                                       not null default true,
    is_deleted               boolean                                       not null default false,
    deleted_by               bigint references users (id),
    deleted_at               timestamp with time zone
);

create unique index if not exists uq_resort_facility_group_locale
    on resort_facility_group_locales (resort_facility_group_id, locale_id)
    where is_active = true and is_deleted = false;
