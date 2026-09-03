-- One-time codes for POST /api/v1/auth/forgot-password + POST /api/v1/auth/reset-password. A user (including a
-- customer auto-registered by the booker's POS flow, see V45/V46) proves ownership of their username by
-- supplying the code sent to it (email or WhatsApp), then sets their real password — the only way an
-- auto-registered customer's random password ever becomes usable.
create table if not exists password_reset_otps
(
    id         bigserial primary key,

    user_id    bigint references users (id) on delete cascade not null,

    otp_code   varchar(6)                                     not null,

    expires_at timestamp with time zone                       not null,

    -- null while unused; set once the code is redeemed via POST /reset-password so it can't be replayed even
    -- if it hasn't expired yet.
    used_at    timestamp with time zone,

    created_by bigint references users (id)                   not null,
    created_at timestamp with time zone                       not null default current_timestamp,
    updated_by bigint references users (id)                   not null,
    updated_at timestamp with time zone                       not null default current_timestamp,
    version    bigint                                         not null default 0,
    is_active  boolean                                        not null default true,
    is_deleted boolean                                        not null default false,
    deleted_by bigint references users (id),
    deleted_at timestamp with time zone
);

create index if not exists idx_password_reset_otps_user
    on password_reset_otps (user_id);
