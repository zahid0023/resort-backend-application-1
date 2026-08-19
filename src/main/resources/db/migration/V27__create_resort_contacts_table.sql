create table if not exists resort_contacts
(
    id                        bigserial primary key,

    -- resort
    resort_id                 bigint references resorts (id)                 not null,

    -- contact purpose
    -- examples:
    -- GENERAL
    -- RESERVATION
    -- SALES
    -- SUPPORT
    -- EMERGENCY
    contact_type_id           bigint references contact_types (id)           not null,

    -- communication channel
    -- examples:
    -- PHONE
    -- MOBILE
    -- EMAIL
    -- WHATSAPP
    -- WEBSITE
    -- FACEBOOK
    communication_channel_id  bigint references communication_channels (id)  not null,

    -- actual contact information.
    -- examples:
    -- +8801712345678
    -- info@resort.com
    -- https://www.myresort.com
    -- https://facebook.com/myresort
    contact_value             text                                           not null,

    -- indicates the preferred contact for the same
    -- purpose and communication channel.
    is_primary                boolean                                        not null default false,

    -- display order.
    sort_order                integer                                        not null default 0,

    created_by                bigint references users (id)                   not null,
    created_at                timestamp with time zone                       not null default current_timestamp,
    updated_by                bigint references users (id)                   not null,
    updated_at                timestamp with time zone                       not null default current_timestamp,
    version                   bigint                                         not null default 0,
    is_active                 boolean                                        not null default true,
    is_deleted                boolean                                        not null default false,
    deleted_by                bigint references users (id),
    deleted_at                timestamp with time zone
);

create unique index if not exists uq_resort_contact
    on resort_contacts (resort_id, contact_type_id, communication_channel_id, contact_value)
    where is_active = true and is_deleted = false;

-- only one primary contact is allowed for each
-- resort + purpose + communication channel.
create unique index if not exists uq_resort_primary_contact
    on resort_contacts (resort_id, contact_type_id, communication_channel_id)
    where is_primary = true and is_active = true and is_deleted = false;
