-- Which specific provider was used to settle a payment under a given payment_method (V52) — e.g. BKASH/NAGAD
-- under MOBILE_BANKING, and (later) VISA/MASTERCARD under CARD or SSLCOMMERZ/STRIPE under ONLINE_GATEWAY.
-- payment_method_id below makes that ownership a real FK rather than a comment-only convention, so a provider
-- row is always structurally tied to the one payment_method it belongs under. Owned exclusively by
-- resort_booking_payments.payment_provider_id (V55) — mirrors the payment_methods (V52) lookup+locale shape
-- exactly, kept as its own table (rather than a free-text column) so the set of valid providers stays
-- admin-managed and filterable/reportable rather than a loose string.
create table if not exists payment_providers
(
    id                bigserial primary key,

    payment_method_id bigint references payment_methods (id) not null,

    -- Internal code.
    -- Examples:
    -- BKASH
    -- NAGAD
    -- ROCKET
    -- UPAY
    -- OTHER
    code              varchar(50)                            not null,
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

-- Scoped by payment_method_id (not code alone) — a code like OTHER can legitimately exist under more than one
-- payment method.
create unique index if not exists uq_payment_providers_method_code
    on payment_providers (payment_method_id, code)
    where is_active = true and is_deleted = false;

create index if not exists idx_payment_providers_method
    on payment_providers (payment_method_id);

create table if not exists payment_provider_locales
(
    id                  bigserial primary key,

    payment_provider_id bigint references payment_providers (id) not null,
    locale_id           bigint references locales (id)           not null,

    -- localized display name.
    -- Examples:
    -- bKash
    -- Nagad
    -- Rocket
    name                varchar(150)                             not null,
    description         text                                     not null default '',
    -- display order in admin ui.
    sort_order          integer                                  not null default 0,

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

create unique index if not exists uq_payment_provider_locale
    on payment_provider_locales (payment_provider_id, locale_id)
    where is_active = true and is_deleted = false;

-- seed: payment providers (all under MOBILE_BANKING for now)
insert into payment_providers (payment_method_id, code, sort_order, created_by, updated_by)
values ((select id from payment_methods where code = 'MOBILE_BANKING'), 'BKASH', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_methods where code = 'MOBILE_BANKING'), 'NAGAD', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_methods where code = 'MOBILE_BANKING'), 'ROCKET', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_methods where code = 'MOBILE_BANKING'), 'UPAY', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_methods where code = 'MOBILE_BANKING'), 'OTHER', 5,
        (select id from users where username = 'system'), (select id from users where username = 'system'));

-- seed: payment provider locales (english)
insert into payment_provider_locales (payment_provider_id, locale_id, name, description, sort_order,
                                      created_by, updated_by)
values ((select id from payment_providers where code = 'BKASH'), (select id from locales where code = 'en'),
        'bKash', 'Payment settled through the bKash mobile financial service.', 1,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_providers where code = 'NAGAD'), (select id from locales where code = 'en'),
        'Nagad', 'Payment settled through the Nagad mobile financial service.', 2,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_providers where code = 'ROCKET'), (select id from locales where code = 'en'),
        'Rocket', 'Payment settled through the Rocket mobile financial service.', 3,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_providers where code = 'UPAY'), (select id from locales where code = 'en'),
        'Upay', 'Payment settled through the Upay mobile financial service.', 4,
        (select id from users where username = 'system'), (select id from users where username = 'system')),
       ((select id from payment_providers where code = 'OTHER'), (select id from locales where code = 'en'),
        'Other', 'Payment settled through a mobile financial service not covered above.', 5,
        (select id from users where username = 'system'), (select id from users where username = 'system'));
