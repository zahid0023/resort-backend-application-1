-- See V1.1 for why this is a dedicated table rather than a column on `users` or a generic contacts table.
create table if not exists user_phones
(
    id          bigserial primary key,

    user_id     bigint references users (id) on delete cascade not null,

    phone       varchar(50)                                    not null,

    -- whether this number is also reachable on WhatsApp — checked by the manual reservation flow to decide
    -- whether generated login credentials can be sent over WhatsApp in addition to SMS.
    is_whatsapp boolean                                         not null default false,

    -- preferred phone for this user; used when only one number should be contacted.
    is_primary  boolean                                        not null default false,

    sort_order  integer                                        not null default 0,

    created_by  bigint references users (id)                   not null,
    created_at  timestamp with time zone                       not null default current_timestamp,
    updated_by  bigint references users (id)                   not null,
    updated_at  timestamp with time zone                       not null default current_timestamp,
    version     bigint                                         not null default 0,
    is_active   boolean                                        not null default true,
    is_deleted  boolean                                        not null default false,
    deleted_by  bigint references users (id),
    deleted_at  timestamp with time zone
);

create unique index if not exists uq_user_phones_user_phone
    on user_phones (user_id, phone)
    where is_active = true and is_deleted = false;

-- Platform-wide: a phone number identifies at most one user — see V1.1's equivalent note for email.
create unique index if not exists uq_user_phones_phone_global
    on user_phones (phone)
    where is_active = true and is_deleted = false;

-- At most one primary phone per user.
create unique index if not exists uq_user_phones_primary
    on user_phones (user_id)
    where is_primary = true and is_active = true and is_deleted = false;
