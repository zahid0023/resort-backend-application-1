create table if not exists bed_types
(
    id         bigserial primary key,

    -- SINGLE
    -- TWIN
    -- DOUBLE
    -- QUEEN
    -- KING
    -- SOFA
    -- BUNK
    -- FUTON
    -- MURPHY
    code       varchar(50)                           not null,
    sort_order integer     default 0                 not null,

    created_by bigint                                not null
        references users (id),
    created_at timestamptz default current_timestamp not null,
    updated_by bigint                                not null
        references users (id),
    updated_at timestamptz default current_timestamp not null,
    version    bigint      default 0                 not null,
    is_active  boolean     default true              not null,
    is_deleted boolean     default false             not null,
    deleted_by bigint
        references users (id),
    deleted_at timestamptz,

    constraint uq_bed_type_code
        unique (code)
);

create table if not exists bed_type_locales
(
    id          bigserial primary key,

    bed_type_id bigint                                not null
        references bed_types (id)
            on delete cascade,

    locale_id   bigint                                not null
        references locales (id),

    -- King Bed
    -- Queen Bed
    -- Twin Bed
    name        varchar(100)                          not null,
    description text        default ''                not null,
    sort_order  integer     default 0                 not null,

    created_by  bigint                                not null
        references users (id),
    created_at  timestamptz default current_timestamp not null,
    updated_by  bigint                                not null
        references users (id),
    updated_at  timestamptz default current_timestamp not null,
    version     bigint      default 0                 not null,
    is_active   boolean     default true              not null,
    is_deleted  boolean     default false             not null,
    deleted_by  bigint
        references users (id),
    deleted_at  timestamptz,

    constraint uq_bed_type_locale
        unique (bed_type_id, locale_id)
);

DO
$$
    DECLARE
        sys_id bigint;
    BEGIN
        SELECT id INTO sys_id FROM users WHERE username = 'system';

        -- =============================================
        -- 1. Bed Types
        -- =============================================
        INSERT INTO bed_types (code, sort_order, created_by, updated_by)
        VALUES ('SINGLE', 1, sys_id, sys_id),
               ('TWIN',   2, sys_id, sys_id),
               ('DOUBLE', 3, sys_id, sys_id),
               ('QUEEN',  4, sys_id, sys_id),
               ('KING',   5, sys_id, sys_id),
               ('SOFA',   6, sys_id, sys_id),
               ('BUNK',   7, sys_id, sys_id),
               ('FUTON',  8, sys_id, sys_id),
               ('MURPHY', 9, sys_id, sys_id)
        ON CONFLICT (code) DO NOTHING;

        -- =============================================
        -- 2. Bed Type Locales (English)
        -- =============================================
        INSERT INTO bed_type_locales (bed_type_id, locale_id, name, description, sort_order, created_by, updated_by)
        SELECT bt.id, l.id, v.name, v.description, v.sort_order, sys_id, sys_id
        FROM bed_types bt
                 JOIN (VALUES
            ('SINGLE', 'en', 'Single Bed',  'A bed designed for one person, typically 90 × 190 cm.',                          1),
            ('TWIN',   'en', 'Twin Bed',    'Two separate single beds in the same room, each typically 90 × 190 cm.',         1),
            ('DOUBLE', 'en', 'Double Bed',  'A bed wide enough for two people, typically 135 × 190 cm.',                      1),
            ('QUEEN',  'en', 'Queen Bed',   'A large bed suitable for two people, typically 150 × 200 cm.',                   1),
            ('KING',   'en', 'King Bed',    'The largest standard bed size, typically 180 × 200 cm.',                         1),
            ('SOFA',   'en', 'Sofa Bed',    'A sofa that folds out into a sleeping surface, ideal for flexible use.',         1),
            ('BUNK',   'en', 'Bunk Bed',    'Two beds stacked vertically, commonly used in dormitories or family rooms.',     1),
            ('FUTON',  'en', 'Futon',       'A low Japanese-style bed or mattress that can be rolled up when not in use.',    1),
            ('MURPHY', 'en', 'Murphy Bed',  'A wall bed that folds into the wall to save floor space when not in use.',       1)
        ) v(code, locale_code, name, description, sort_order)
                      ON bt.code = v.code
                 JOIN locales l ON l.code = v.locale_code
        ON CONFLICT (bed_type_id, locale_id) DO NOTHING;

    END
$$;