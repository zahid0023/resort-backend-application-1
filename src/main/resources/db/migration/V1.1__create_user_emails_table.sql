-- A user (admin, resort owner/booker, or customer — can have
-- more than one email address, so this is its own table rather than a single `email` column on `users`. Split
-- from phones (V1.2) rather than a generic "user_contacts" table for normalization: an email and a phone
-- number are different data with different validation, not two values of the same shape.
create table if not exists user_emails
(
    id         bigserial primary key,

    user_id    bigint references users (id) on delete cascade not null,

    email      varchar(255)                                   not null,

    -- preferred email for this user; used when only one email should be contacted (e.g. sending generated
    -- login credentials).
    is_primary boolean                                        not null default false,

    sort_order integer                                        not null default 0,

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

create unique index if not exists uq_user_emails_user_email
    on user_emails (user_id, email)
    where is_active = true and is_deleted = false;

-- Platform-wide: an email address identifies at most one user. This is what makes "look this email up to
-- find-or-create a customer" (see the reservations migration's note on the manual reservation flow) an
-- unambiguous single match instead of a search across possibly-multiple accounts.
create unique index if not exists uq_user_emails_email_global
    on user_emails (email)
    where is_active = true and is_deleted = false;

-- At most one primary email per user.
create unique index if not exists uq_user_emails_primary
    on user_emails (user_id)
    where is_primary = true and is_active = true and is_deleted = false;
