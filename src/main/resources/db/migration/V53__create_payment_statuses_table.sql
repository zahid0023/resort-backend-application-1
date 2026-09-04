-- Payment lifecycle status. Owned exclusively by resort_booking_payments.payment_status_id (V54) — mirrors the
-- reservation_statuses (V44) lookup+locale shape exactly.
create table if not exists payment_statuses
(
    id         bigserial primary key,

    -- Internal code.
    -- Examples:
    -- PENDING
    -- COMPLETED
    -- FAILED
    -- REFUNDED
    -- PARTIALLY_REFUNDED
    code       varchar(50)                  not null,
    -- display order in admin ui.
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

create unique index if not exists uq_payment_statuses_code
    on payment_statuses (code)
    where is_active = true and is_deleted = false;

create table if not exists payment_status_locales
(
    id                bigserial primary key,

    payment_status_id bigint references payment_statuses (id) not null,
    locale_id         bigint references locales (id)          not null,

    -- localized display name.
    -- Examples:
    -- Pending
    -- Completed
    -- Refunded
    name              varchar(150)                            not null,
    description       text                                    not null default '',
    -- display order in admin ui.
    sort_order        integer                                 not null default 0,

    created_by        bigint references users (id)            not null,
    created_at        timestamp with time zone                not null default current_timestamp,
    updated_by        bigint references users (id)            not null,
    updated_at        timestamp with time zone                not null default current_timestamp,
    version           bigint                                  not null default 0,
    is_active         boolean                                 not null default true,
    is_deleted        boolean                                 not null default false,
    deleted_by        bigint references users (id),
    deleted_at        timestamp with time zone
);

create unique index if not exists uq_payment_status_locale
    on payment_status_locales (payment_status_id, locale_id)
    where is_active = true and is_deleted = false;

-- seed: payment statuses
insert into payment_statuses (code, sort_order, created_by, updated_by)
values ('PENDING', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('COMPLETED', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('FAILED', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('REFUNDED', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('PARTIALLY_REFUNDED', 5, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: payment status locales (english)
insert into payment_status_locales (payment_status_id, locale_id, name, description, sort_order,
                                    created_by, updated_by)
values ((select id from payment_statuses where code = 'PENDING'), (select id from locales where code = 'en'),
        'Pending', 'Payment has been initiated but not yet confirmed.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_statuses where code = 'COMPLETED'), (select id from locales where code = 'en'),
        'Completed', 'Payment has been received and confirmed in full.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_statuses where code = 'FAILED'), (select id from locales where code = 'en'),
        'Failed', 'Payment attempt did not go through.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_statuses where code = 'REFUNDED'), (select id from locales where code = 'en'),
        'Refunded', 'Payment was fully refunded to the customer.', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_statuses where code = 'PARTIALLY_REFUNDED'), (select id from locales where code = 'en'),
        'Partially Refunded', 'Part of the payment was refunded to the customer.', 5,
        (select id from users where username = 'system'), (select id from users where username = 'system'));
