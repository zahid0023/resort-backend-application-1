create table if not exists price_types
(
    id         bigserial primary key,

    -- stable business code used internally by the application.
    -- examples:
    -- BAS
    -- WKD
    -- WKE
    -- HOL
    code       varchar(50)                  not null unique,
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

create table if not exists price_type_locales
(
    id            bigserial primary key,

    -- parent price type.
    price_type_id bigint references price_types (id) not null,
    -- translation language.
    locale_id     bigint references locales (id) not null,

    -- localized display name.
    -- example:
    -- "Weekend Price"
    name          varchar(100)                 not null,
    -- short explanation shown in ui.
    -- example:
    -- "Price applied to bookings made on Saturdays and Sundays."
    description   text                         not null default '',
    -- display order.
    sort_order    integer                      not null default 0,

    -- business purpose of this price type.
    -- example:
    -- "Allows higher pricing during weekends."
    purpose       text                         not null default '',
    -- example scenario shown to administrators.
    -- example:
    -- "Football Ground A costs $50/hour on weekdays and $70/hour on weekends."
    usage_example text                         not null default '',

    created_by    bigint references users (id) not null,
    created_at    timestamp with time zone     not null default current_timestamp,
    updated_by    bigint references users (id) not null,
    updated_at    timestamp with time zone     not null default current_timestamp,
    version       bigint                       not null default 0,
    is_active     boolean                      not null default true,
    is_deleted    boolean                      not null default false,
    deleted_by    bigint references users (id),
    deleted_at    timestamp with time zone,

    constraint uq_price_type_locale
        unique (price_type_id, locale_id)
);

create table if not exists price_type_scope_assignments
(
    id                  bigserial primary key,

    price_type_scope_id bigint references price_type_scopes (id) not null,
    price_type_id       bigint references price_types (id)       not null,

    created_by          bigint references users (id)             not null,
    created_at          timestamp with time zone                 not null default current_timestamp,
    updated_by          bigint references users (id)             not null,
    updated_at          timestamp with time zone                 not null default current_timestamp,
    version             bigint                                   not null default 0,
    is_active           boolean                                  not null default true,
    is_deleted          boolean                                  not null default false,
    deleted_by          bigint references users (id),
    deleted_at          timestamp with time zone
);

-- seed: price types
insert into price_types (code, sort_order, created_by, updated_by)
values ('BAS', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('WKD', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('WKE', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('HOL', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('SPECIAL', 5, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: price type locales (english)
insert into price_type_locales (price_type_id, locale_id, name, description, sort_order, purpose, usage_example,
                                created_by, updated_by)
values ((select id from price_types where code = 'BAS'), (select id from locales where code = 'en'),
        'Base Price', 'Standard price applied by default when no other price type matches.', 1,
        'Serves as the fallback pricing rule for all bookings.',
        'A standard room costs $100/night under the base price.',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from price_types where code = 'WKD'), (select id from locales where code = 'en'),
        'Weekday Price', 'Price applied to bookings made on weekdays (Monday through Friday).', 2,
        'Allows lower pricing during off-peak weekday periods.',
        'A room costs $90/night on weekdays compared to $130/night on weekends.',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from price_types where code = 'WKE'), (select id from locales where code = 'en'),
        'Weekend Price', 'Price applied to bookings made on Saturdays and Sundays.', 3,
        'Allows higher pricing during peak weekend demand.',
        'Football Ground A costs $50/hour on weekdays and $70/hour on weekends.',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from price_types where code = 'HOL'), (select id from locales where code = 'en'),
        'Holiday Price', 'Price applied to bookings on public holidays or designated holiday periods.', 4,
        'Enables premium pricing during high-demand holiday seasons.',
        'A deluxe room costs $200/night during the Christmas holiday period.',
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from price_types where code = 'SPECIAL'), (select id from locales where code = 'en'),
        'Special Price', 'Price applied during special promotions, events, or limited-time offers.', 5,
        'Enables custom pricing for marketing campaigns and one-off events.',
        'A suite costs $150/night during a New Year''s Eve special promotion, down from the usual $220/night.',
        (select id from users where username = 'system'), (select id from users where username = 'system'));
