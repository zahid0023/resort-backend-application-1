create table if not exists days_of_week
(
    id             bigserial primary key,

    -- Internal code.
    -- Examples:
    -- MONDAY
    -- TUESDAY
    -- WEDNESDAY
    -- THURSDAY
    -- FRIDAY
    -- SATURDAY
    -- SUNDAY
    code           varchar(50)                                        not null unique,

    -- ISO-8601 day number.
    -- Monday = 1
    -- Tuesday = 2
    -- Wednesday = 3
    -- Thursday = 4
    -- Friday = 5
    -- Saturday = 6
    -- Sunday = 7
    iso_day_number smallint                                           not null,

    -- Display order.
    display_order  smallint                                           not null,

    created_by     bigint                                             not null references users (id),
    created_at     timestamp with time zone default current_timestamp not null,
    updated_by     bigint                                             not null references users (id),
    updated_at     timestamp with time zone default current_timestamp not null,
    version        bigint                   default 0                 not null,
    is_active      boolean                  default true              not null,
    is_deleted     boolean                  default false             not null,
    deleted_by     bigint references users (id),
    deleted_at     timestamp with time zone,

    constraint chk_days_of_week_iso
        check (iso_day_number between 1 and 7),

    constraint uq_days_of_week_iso
        unique (iso_day_number),

    constraint uq_days_of_week_display_order
        unique (display_order)
);

create index idx_days_of_week_code
    on days_of_week (code);

create table if not exists days_of_week_locales
(
    id             bigserial primary key,

    day_of_week_id bigint                                             not null
        references days_of_week (id) on delete cascade,

    locale_id      bigint                                             not null
        references locales (id),

    name           varchar(100)                                       not null,

    short_name     varchar(20),

    created_by     bigint                                             not null references users (id),
    created_at     timestamp with time zone default current_timestamp not null,
    updated_by     bigint                                             not null references users (id),
    updated_at     timestamp with time zone default current_timestamp not null,
    version        bigint                   default 0                 not null,
    is_active      boolean                  default true              not null,
    is_deleted     boolean                  default false             not null,
    deleted_by     bigint references users (id),
    deleted_at     timestamp with time zone,

    constraint uq_days_of_week_locale
        unique (day_of_week_id, locale_id)
);

create index idx_days_of_week_locales_day
    on days_of_week_locales (day_of_week_id);

create index idx_days_of_week_locales_locale
    on days_of_week_locales (locale_id);

DO
$$
    DECLARE
        sys_id bigint;
    BEGIN
        SELECT id INTO sys_id FROM users WHERE username = 'system';

        -- =============================================
        -- 1. Days of Week
        -- =============================================
        INSERT INTO days_of_week (code, iso_day_number, display_order, created_by, updated_by)
        VALUES ('MONDAY',    1, 1, sys_id, sys_id),
               ('TUESDAY',   2, 2, sys_id, sys_id),
               ('WEDNESDAY', 3, 3, sys_id, sys_id),
               ('THURSDAY',  4, 4, sys_id, sys_id),
               ('FRIDAY',    5, 5, sys_id, sys_id),
               ('SATURDAY',  6, 6, sys_id, sys_id),
               ('SUNDAY',    7, 7, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        -- =============================================
        -- 2. Days of Week Locales (English)
        -- =============================================
        INSERT INTO days_of_week_locales (day_of_week_id, locale_id, name, short_name, created_by, updated_by)
        SELECT d.id, l.id, v.name, v.short_name, sys_id, sys_id
        FROM days_of_week d
                 JOIN (VALUES ('MONDAY',    'en', 'Monday',    'Mon'),
                              ('TUESDAY',   'en', 'Tuesday',   'Tue'),
                              ('WEDNESDAY', 'en', 'Wednesday', 'Wed'),
                              ('THURSDAY',  'en', 'Thursday',  'Thu'),
                              ('FRIDAY',    'en', 'Friday',    'Fri'),
                              ('SATURDAY',  'en', 'Saturday',  'Sat'),
                              ('SUNDAY',    'en', 'Sunday',    'Sun'))
                          v(code, locale_code, name, short_name)
                      ON d.code = v.code
                 JOIN locales l ON l.code = v.locale_code
        ON CONFLICT (day_of_week_id, locale_id) DO NOTHING;

    END
$$;