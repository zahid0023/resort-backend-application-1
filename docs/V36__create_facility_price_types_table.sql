create table if not exists facility_price_types
(
    id         bigserial primary key,

    -- Stable business code.
    -- Examples:
    -- FREE
    -- INCLUDED
    -- PAID
    -- CONTACT
    code       varchar(50)              not null unique,
    -- Display order.
    sort_order integer                  not null default 0,

    created_by bigint                   not null
        references users (id),
    created_at timestamp with time zone not null default current_timestamp,
    updated_by bigint                   not null
        references users (id),
    updated_at timestamp with time zone not null default current_timestamp,
    version    bigint                   not null default 0,
    is_active  boolean                  not null default true,
    is_deleted boolean                  not null default false,
    deleted_by bigint references users (id),
    deleted_at timestamp with time zone
);

create table if not exists facility_price_type_locales
(
    id                     bigserial primary key,

    facility_price_type_id bigint                   not null
        references facility_price_types (id)
            on delete cascade,

    locale_id              bigint                   not null
        references locales (id)
            on delete cascade,

    -- Localized display name.
    name                   varchar(100)             not null,

    -- Description shown in the UI.
    description            text                     not null default '',

    sort_order             integer                  not null default 0,

    -- Explains when this pricing type should be used.
    purpose                text                     not null default '',

    -- Example shown to administrators.
    usage_example          text                     not null default '',

    created_by             bigint                   not null
        references users (id),
    created_at             timestamp with time zone not null default current_timestamp,
    updated_by             bigint                   not null
        references users (id),
    updated_at             timestamp with time zone not null default current_timestamp,
    version                bigint                   not null default 0,
    is_active              boolean                  not null default true,
    is_deleted             boolean                  not null default false,
    deleted_by             bigint references users (id),
    deleted_at             timestamp with time zone,
    constraint uq_facility_price_type_locale
        unique (facility_price_type_id, locale_id)
);

DO
$$
    DECLARE
        sys_id bigint;
    BEGIN
        SELECT id INTO sys_id FROM users WHERE username = 'system';

        -- =============================================
        -- 1. Facility Price Types
        -- =============================================
        INSERT INTO facility_price_types (code, sort_order, created_by, updated_by)
        VALUES ('FREE',     1, sys_id, sys_id),
               ('INCLUDED', 2, sys_id, sys_id),
               ('PAID',     3, sys_id, sys_id),
               ('CONTACT',  4, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        -- =============================================
        -- 2. Facility Price Type Locales (English)
        -- =============================================
        INSERT INTO facility_price_type_locales (facility_price_type_id, locale_id, name, description, sort_order,
                                                 purpose, usage_example, created_by, updated_by)
        SELECT fpt.id, l.id, v.name, v.description, fpt.sort_order, v.purpose, v.usage_example, sys_id, sys_id
        FROM facility_price_types fpt
                 JOIN (VALUES ('FREE', 'en',
                               'Free',
                               'The facility is available at no charge to guests.',
                               'Used for complimentary amenities included with the stay.',
                               'The gym and outdoor pool are free for all guests.'),
                              ('INCLUDED', 'en',
                               'Included',
                               'The facility is bundled into the room or package price.',
                               'Used when the cost is absorbed into an existing booking package.',
                               'Breakfast and airport shuttle are included in the full-board package.'),
                              ('PAID', 'en',
                               'Paid',
                               'The facility requires a separate fee at the time of booking or use.',
                               'Used for premium amenities or services that are charged individually.',
                               'A spa treatment costs $80 per session and must be booked separately.'),
                              ('CONTACT', 'en',
                               'Contact for Price',
                               'Pricing is not fixed and must be confirmed directly with the resort.',
                               'Used for customised services where pricing depends on specific requirements.',
                               'Private yacht charter pricing is available on request — contact the resort for details.'))
                          v(code, locale_code, name, description, purpose, usage_example)
                      ON fpt.code = v.code
                 JOIN locales l ON l.code = v.locale_code
        ON CONFLICT (facility_price_type_id, locale_id) DO NOTHING;

    END
$$;