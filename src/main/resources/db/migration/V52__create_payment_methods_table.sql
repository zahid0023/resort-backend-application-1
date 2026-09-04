-- How a payment was actually settled (CASH, CARD, ...), independent of which booking it belongs to or what
-- its outcome was (see payment_statuses, V53). Owned exclusively by resort_booking_payments.payment_method_id
-- (V54) — mirrors the booking_sources (V43) lookup+locale shape exactly.
create table if not exists payment_methods
(
    id         bigserial primary key,

    -- Internal code.
    -- Examples:
    -- CASH
    -- CARD
    -- BANK_TRANSFER
    -- MOBILE_BANKING
    -- ONLINE_GATEWAY
    -- OTHER
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

create unique index if not exists uq_payment_methods_code
    on payment_methods (code)
    where is_active = true and is_deleted = false;

create table if not exists payment_method_locales
(
    id                bigserial primary key,

    payment_method_id bigint references payment_methods (id) not null,
    locale_id         bigint references locales (id)         not null,

    -- localized display name.
    -- Examples:
    -- Cash
    -- Card
    -- Bank Transfer
    name              varchar(150)                           not null,
    description       text                                   not null default '',
    -- display order in admin ui.
    sort_order        integer                                not null default 0,

    created_by        bigint references users (id)           not null,
    created_at        timestamp with time zone               not null default current_timestamp,
    updated_by        bigint references users (id)           not null,
    updated_at        timestamp with time zone               not null default current_timestamp,
    version           bigint                                 not null default 0,
    is_active         boolean                                not null default true,
    is_deleted        boolean                                not null default false,
    deleted_by        bigint references users (id),
    deleted_at        timestamp with time zone
);

create unique index if not exists uq_payment_method_locale
    on payment_method_locales (payment_method_id, locale_id)
    where is_active = true and is_deleted = false;

-- seed: payment methods
insert into payment_methods (code, sort_order, created_by, updated_by)
values ('CASH', 1, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('CARD', 2, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('BANK_TRANSFER', 3, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('MOBILE_BANKING', 4, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('ONLINE_GATEWAY', 5, (select id from users where username = 'system'),
        (select id from users where username = 'system')),
       ('OTHER', 6, (select id from users where username = 'system'),
        (select id from users where username = 'system'));

-- seed: payment method locales (english)
insert into payment_method_locales (payment_method_id, locale_id, name, description, sort_order,
                                    created_by, updated_by)
values ((select id from payment_methods where code = 'CASH'), (select id from locales where code = 'en'),
        'Cash', 'Payment settled in physical cash.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_methods where code = 'CARD'), (select id from locales where code = 'en'),
        'Card', 'Payment settled by debit or credit card.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_methods where code = 'BANK_TRANSFER'), (select id from locales where code = 'en'),
        'Bank Transfer', 'Payment settled by direct bank transfer.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_methods where code = 'MOBILE_BANKING'), (select id from locales where code = 'en'),
        'Mobile Banking', 'Payment settled through a mobile financial service (e.g. bKash, Nagad).', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_methods where code = 'ONLINE_GATEWAY'), (select id from locales where code = 'en'),
        'Online Gateway', 'Payment settled through an online payment gateway.', 5,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_methods where code = 'OTHER'), (select id from locales where code = 'en'),
        'Other', 'Payment settled by a method not covered above.', 6,
        (select id from users where username = 'system'), (select id from users where username = 'system'));
