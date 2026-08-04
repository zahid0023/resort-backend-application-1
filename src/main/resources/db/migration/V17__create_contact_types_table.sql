create table if not exists contact_types
(
    id         bigserial primary key,

    -- stable business code used internally by the application.
    -- examples:
    -- GENERAL
    -- RESERVATION
    -- SALES
    -- SUPPORT
    -- EMERGENCY
    -- ACCOUNTING
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

create table if not exists contact_type_locales
(
    id              bigserial primary key,

    -- parent contact type.
    contact_type_id bigint references contact_types (id) not null,
    -- translation language.
    locale_id       bigint references locales (id) not null,

    -- localized display name.
    -- example:
    -- "General"
    name            varchar(100)                 not null,
    -- short explanation shown in ui.
    -- example:
    -- "Main contact for general enquiries about the resort."
    description     text                         not null default '',
    -- display order.
    sort_order      integer                      not null default 0,

    created_by      bigint references users (id) not null,
    created_at      timestamp with time zone     not null default current_timestamp,
    updated_by      bigint references users (id) not null,
    updated_at      timestamp with time zone     not null default current_timestamp,
    version         bigint                       not null default 0,
    is_active       boolean                      not null default true,
    is_deleted      boolean                      not null default false,
    deleted_by      bigint references users (id),
    deleted_at      timestamp with time zone,

    constraint uq_contact_type_locale
        unique (contact_type_id, locale_id)
);

-- seed: contact types
insert into contact_types (code, sort_order, created_by, updated_by)
values ('GENERAL', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('RESERVATION', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('SALES', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('SUPPORT', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('EMERGENCY', 5, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('ACCOUNTING', 6, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: contact type locales (english)
insert into contact_type_locales (contact_type_id, locale_id, name, description, sort_order,
                                  created_by, updated_by)
values ((select id from contact_types where code = 'GENERAL'), (select id from locales where code = 'en'),
        'General', 'Main contact for general enquiries about the resort.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from contact_types where code = 'RESERVATION'), (select id from locales where code = 'en'),
        'Reservation', 'Contact for room bookings, availability checks, and reservation changes.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from contact_types where code = 'SALES'), (select id from locales where code = 'en'),
        'Sales', 'Contact for group bookings, corporate deals, and promotional inquiries.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from contact_types where code = 'SUPPORT'), (select id from locales where code = 'en'),
        'Support', 'Contact for guest assistance, complaints, and on-site support during stay.', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from contact_types where code = 'EMERGENCY'), (select id from locales where code = 'en'),
        'Emergency', '24/7 emergency contact for urgent situations requiring immediate assistance.', 5,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from contact_types where code = 'ACCOUNTING'), (select id from locales where code = 'en'),
        'Accounting', 'Contact for billing, invoices, and payment-related queries.', 6,
        (select id from users where username = 'system'), (select id from users where username = 'system'));
