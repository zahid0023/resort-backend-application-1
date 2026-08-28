-- Classifies how a facility is priced (FREE/INCLUDED/FIXED/VARIABLE) — a shared lookup usable by any
-- facility-pricing table (currently resort_facility_prices, V31; intended to also back room-category-facility
-- and room-facility pricing as those are added). Resort room category/room base+special pricing (V35/V40) has
-- its own dedicated main/special tables and never references this; the resort weekly schedule's WEEKDAY/WEEKEND
-- classification (V24.1) is a native Postgres enum, not a row here either.
create table if not exists facility_price_types
(
    id         bigserial primary key,

    -- stable business code used internally by the application.
    -- examples:
    -- FREE
    -- INCLUDED
    -- FIXED
    -- VARIABLE
    code       varchar(50)                  not null,
    -- display order in administrative interfaces.
    sort_order integer                      not null default 0,

    created_by bigint references users (id) not null,
    created_at timestamp with time zone     not null default current_timestamp,
    updated_by bigint references users (id) not null,
    updated_at timestamp with time zone     not null default current_timestamp,
    version    bigint                       not null default 0,
    is_active  boolean                      not null default true,
    is_deleted boolean                      not null default false,
    deleted_by bigint references users (id),
    deleted_at timestamp with time zone
);

create unique index if not exists uq_facility_price_types_code
    on facility_price_types (code)
    where is_active = true and is_deleted = false;

create table if not exists facility_price_type_locales
(
    id                          bigserial primary key,

    -- parent facility price type.
    facility_price_type_id     bigint references facility_price_types (id) not null,
    -- translation language.
    locale_id                  bigint references locales (id)              not null,

    -- localized display name.
    -- example:
    -- "Fixed Price"
    name          varchar(100)                       not null,
    -- short explanation shown in ui.
    -- example:
    -- "A flat, fixed amount is charged for using the facility."
    description   text                               not null default '',
    -- display order.
    sort_order    integer                            not null default 0,

    -- business purpose of this price type.
    -- example:
    -- "Standard flat-rate billing for a facility."
    purpose       text                               not null default '',
    -- example scenario shown to administrators.
    -- example:
    -- "The gym costs a FIXED $10 per day."
    usage_example text                               not null default '',

    created_by    bigint references users (id)       not null,
    created_at    timestamp with time zone           not null default current_timestamp,
    updated_by    bigint references users (id)       not null,
    updated_at    timestamp with time zone           not null default current_timestamp,
    version       bigint                             not null default 0,
    is_active     boolean                            not null default true,
    is_deleted    boolean                            not null default false,
    deleted_by    bigint references users (id),
    deleted_at    timestamp with time zone
);

create unique index if not exists uq_facility_price_type_locale
    on facility_price_type_locales (facility_price_type_id, locale_id)
    where is_active = true and is_deleted = false;

-- seed: facility price types
insert into facility_price_types (code, sort_order, created_by, updated_by)
values ('FREE', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('INCLUDED', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('FIXED', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('VARIABLE', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: facility price type locales (english)
insert into facility_price_type_locales (facility_price_type_id, locale_id, name, description,
                                          sort_order, purpose, usage_example, created_by, updated_by)
values ((select id from facility_price_types where code = 'FREE'), (select id from locales where code = 'en'),
        'Free', 'The facility is provided to guests at no additional cost.', 1,
        'Lets a resort advertise a facility as complimentary.',
        'The rooftop lounge is FREE for all guests to use.',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_price_types where code = 'INCLUDED'), (select id from locales where code = 'en'),
        'Included', 'The facility''s cost is already bundled into another charge (e.g. the room rate) and is not billed separately.', 2,
        'Distinguishes a bundled-in facility from one that is free with no cost anywhere.',
        'Breakfast is INCLUDED in the nightly room rate.',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_price_types where code = 'FIXED'), (select id from locales where code = 'en'),
        'Fixed', 'A flat, fixed amount is charged for using the facility, per the selected price unit.', 3,
        'Standard flat-rate billing for a facility.',
        'The gym costs a FIXED $10 per day.',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from facility_price_types where code = 'VARIABLE'), (select id from locales where code = 'en'),
        'Variable', 'The price varies (e.g. by season, package, or negotiation); amount may reflect a typical or starting price — see note for details.', 4,
        'Covers facilities whose price cannot be expressed as a single flat rate.',
        'Spa treatments are VARIABLE, starting from $40 depending on the service chosen.',
        (select id from users where username = 'system'), (select id from users where username = 'system'));
